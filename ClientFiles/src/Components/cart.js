import React, {Component} from "react";
import Basket from './basket'
import {connectedPromise, setUser, stompClient, user} from "../App";
import "../App.css" ;
import basket from "./basket";
import wrapRender from "./ModalPurchase";
import ModalPurchase from "./ModalPurchase";

class Cart extends Component{
    state = {
        error: "",
        baskets: []
    };
    mounted = false;

    async componentDidMount() {
        await connectedPromise;
        this.mounted = true;
        stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
        stompClient.subscribe('/user/topic/AddItemToCartResult', (r) => {
            if (this.mounted) {
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
                }
            }
        });
        stompClient.subscribe('/user/topic/removeItemToCartResult', (r) => {
            if (this.mounted) {
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
                }
            }
        });

        stompClient.subscribe('/user/topic/cart/getCartResult', (r) => {
            console.log('!!!!!!!!!!');
            if (this.mounted) {
                console.log('?????????');
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    this.setState({baskets: []});
                    console.log('res.object.baskets');
                    this.setState({baskets: res.object.baskets});
                }
            }
        });
        this.mounted = true;
    }
    componentWillUnmount() {
        this.mounted = false;
        stompClient.unsubscribe('/user/topic/cart/getCartResult');
        stompClient.unsubscribe('/user/topic/removeItemToCartResult');
        stompClient.unsubscribe('/user/topic/AddItemToCartResult');
    }

    handleDelete = (store, item) => {
        stompClient.send("/app/market/removeItemToCart", {}, JSON.stringify({"store_id" : store, "item_id" : item.item_id, "amount": item.amount}));

    };

    handleIncrement = (store, item) => {
        stompClient.send("/app/market/AddItemToCart", {}, JSON.stringify({"store_id" : store, "item_id" : item.item_id, "amount": 1}));
    };

    handleDecrement = (store, item) => {
        stompClient.send("/app/market/removeItemToCart", {}, JSON.stringify({"store_id" : store, "item_id" : item.item_id, "amount": 1}));
    };


    renderBaskets = () => {
        if(this.state.baskets.length === 0) return <p>Your cart is empty</p>;
        return (
            <ul style ={{listStyle:'none'}} >
                {this.state.baskets.map(basket =>
                 (basket.items.length !== 0) ?
                        <li key={"li+"+basket.storeName} className={"basket-grid"}   >
                            <Basket
                                key = {basket.Store_id}
                                store = {basket.Store_id}
                                storeName = {basket.Store_name}
                                items = {basket.items}
                                onDelete = {this.handleDelete}
                                onIncrement = {this.handleIncrement}
                                onDecrement = {this.handleDecrement}
                            />
                        </li> : null
                    )}
            </ul>)
    };
    render() {
        return (
            <React.Fragment >
                <div style={{paddingLeft: "30px"}}>
                    <h1>YOUR CART - {user ? user.userName : "Guest"}</h1>
                    {this.renderBaskets()}

                        <p>{"total: "+ this.getSum()+"   items:"+ this.getAmount()}</p>
                    <ModalPurchase onPurchase = {this.handlePurchase}>
                        Purchase
                    </ModalPurchase>
                </div>
            </React.Fragment>
        );
    }

    handlePurchase = () => {
        stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
    };

    getSum = () => {
        return this.state.baskets.map(b => b.items.
            map(i => i.price * i.amount).reduce((x,y)=> x+y,0)).reduce((x,y)=> x+y,0);
    };
    getAmount = () => {
        return this.state.baskets.map(b => b.items.
        map(i => i.amount).reduce((x,y)=> x+y,0)).reduce((x,y)=> x+y,0);
    };


}

export default Cart;