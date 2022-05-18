import React, {Component} from "react";
import Basket from './basket'
import {connectedPromise, stompClient} from "../App";

class Cart extends Component{
    state = {
        baskets: [{storeName: 'rrr', items: [{id: 'tag1', amount: 0},{id: 'tag2', amount: 0},{id: 'tag3', amount: 0}]}
                    , {storeName: 'Yarden\'s fanBace', items: [{id: 'otograth', amount: 0},{id: 'pick', amount: 0},{id: 'pice of code', amount: 0}]}]
    };

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreInfoResult', (r) => {
            const res = JSON.parse(r["body"]);
            console.log(res);
            // if (res.errorMessage == null)
            // {
            //     this.state.listitems = res.object;
            //     this.setState({[this.state.listitems]: this.state.listitems});
            // }
            // else
            // {
            //     this.state.error = res.errorMessage;
            //     this.setState({[this.state.error]: this.state.error});
            // }
        });
        stompClient.send("/app/market/getStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoreInfoResult');
    }

    handleDelete = (basket, item) => {
        const baskets = [...this.state.baskets];
        const bIndex = baskets.indexOf(basket);
        const items = [...basket.items].filter(i => i.id !== item.id);
        baskets[bIndex] = {...basket};
        baskets[bIndex].items =items;
        this.setState({baskets});
    };

    handleIncrement = (basket, item) => {
        const baskets = [...this.state.baskets];
        const bIndex = baskets.indexOf(basket);
        const items = [...basket.items];
        const index = items.indexOf(item);
        items[index]= {...item};
        items[index].amount++;
        baskets[bIndex] = {...basket};
        baskets[bIndex].items =items;
        this.setState({baskets});
    };


    renderBaskets = () => {
        if(this.state.baskets.length === 0) return <p>Your cart is empty</p>;
        return (
            <ul>
                {this.state.baskets.map(basket =>
                    <li key={"li+"+basket.storeName}>
                        <Basket
                            key={basket.storeName}
                            basket = {basket}
                            onDelete = {this.handleDelete}
                            onIncrement = {this.handleIncrement}
                        />
                    </li>)}
            </ul>)
    };
    render() {
        return (
            <React.Fragment>
                <h1>YOUR CART</h1>
                {this.renderBaskets()}
            </React.Fragment>
        );
    }
}

export default Cart;