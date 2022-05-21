import React, {Component} from "react";
import Basket from './basket'
import {connectedPromise, setUser, stompClient} from "../App";
import "../App.css" ;
import basket from "./basket";

class Cart extends Component{
    state = {
        error: "",
        baskets: []
    };

    async componentDidMount() {
        await connectedPromise;
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
            if (this.mounted) {
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    this.setState({baskets: []});
                    this.setState({baskets: res.object.baskets});
                }
            }
        });
        this.mounted = true;
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
                    <h1>YOUR CART</h1>
                    {this.renderBaskets()}

                        <p>{"total: "+ this.getSum()+"   items:"+ this.getAmount()}</p>

                    <button className={"button-6-delete"} onClick={() => this.handlePurchase()}>
                        Purchase
                    </button>
                </div>
            </React.Fragment>
        );
    }

    handlePurchase = () => {

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