import React, {Component} from "react";
import Basket from './basket'
import Bids from './Bids'
import {connectedPromise, setUser, stompClient, user} from "../App";
import "../App.css" ;
import basket from "./basket";
import wrapRender from "./ModalPurchase";
import ModalPurchase from "./ModalPurchase";
import bids from "./Bids";
import ResultLabel from "./ResultLabel";

class Cart extends Component{
    state = {
        error: "",
        baskets: [],
        bids: [],
        price: 0
    };
    mounted = false;

    async componentDidMount() {
        await connectedPromise;
        this.mounted = true;
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
                    stompClient.send("/app/market/cart/getPrice", {}, JSON.stringify({}));
                }
            }
        });
        stompClient.subscribe('/user/topic/bid/getUserBidsResult', (r) => {
            if (this.mounted) {
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    this.setState({bids: []});
                    this.setState({bids: res.object});
                    console.log(bids)
                }
            }
        });
        stompClient.subscribe('/user/topic/cart/getPriceResult', (r) => {
            if (this.mounted) {
                let res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage;
                this.setState({error: this.state.error});
                if (!this.state.error) {
                    this.setState({price: 0});
                    this.setState({price: res.object});
                }
            }
        });
        stompClient.subscribe('/user/topic/bid/deleteBidResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage;
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/bid/getUserBids", {}, JSON.stringify({}));
            }
        });
        stompClient.subscribe('/user/topic/bid/addBidToCartResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage;
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/bid/getUserBids", {}, JSON.stringify({}));
            }
        });
        stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
        stompClient.send("/app/market/bid/getUserBids", {}, JSON.stringify({}));
        this.mounted = true;
    }
    componentWillUnmount() {
        this.mounted = false;
        stompClient.unsubscribe('/user/topic/cart/getCartResult');
        stompClient.unsubscribe('/user/topic/removeItemToCartResult');
        stompClient.unsubscribe('/user/topic/AddItemToCartResult');
        stompClient.unsubscribe('/user/topic/bid/deleteBidResult');
        stompClient.unsubscribe('/user/topic/bid/getUserBidsResult');
        stompClient.unsubscribe('/user/topic/bid/getPriceResult');
        stompClient.unsubscribe('/user/topic/bid/addBidToCartResult');


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
    handleBidToCart = (store, bid) => {
        stompClient.send("/app/market/AddItemToCart", {}, JSON.stringify({"store_id" : store, "item_id" : bid.item, "amount": bid.amount}));
    };
    getBidsInCart = () =>{
        return this.state.bids.filter(b => b.inCart);
    };



    renderBaskets = () => {
        if(this.state.baskets.length === 0) return <p>Your cart is empty</p>;
        return (
            <div style ={{listStyle:'none'}} >
                {this.state.baskets.map(basket =>
                 (basket.items.length !== 0) ?
                        <div key={"li+"+basket.storeName}    >
                            <Basket
                                key = {basket.Store_id}
                                store = {basket.Store_id}
                                storeName = {basket.Store_name}
                                items = {basket.items}
                                onDelete = {this.handleDelete}
                                onIncrement = {this.handleIncrement}
                                onDecrement = {this.handleDecrement}
                            />
                        </div> : null
                    )}
            </div>)
    };
    render() {
        return (
            <React.Fragment >
                <div>
                    <h2>YOUR CART - {user ? user.userName : "Guest"}</h2>
                    {this.renderBaskets()}
                    {this.getBidsInCart().length !== 0 &&
                        <div>
                            <h4>Bids in cart</h4>
                            <Bids inCart={true} items = {this.getBidsInCart()} onDelete = {this.removeBid} ></Bids>
                        </div>
                    }
                    <p> {"Subtotal ( "+this.getAmount() + (this.getAmount() ===1 ? " item" : " items")+"): "+ this.getSum() +"₪"} <br />
                    {"Discount: "+ (this.getSum() - this.state.price)+"₪"} <br />
                    {"Final price ( "+this.getAmount() + (this.getAmount() ===1 ? " item" : " items")+"): "+ this.state.price +"₪" }
                    </p>
                    <ModalPurchase onPurchase = {this.handlePurchase}>
                        Purchase
                    </ModalPurchase>

                    <h2 style={{marginTop : "32px"}}>YOUR BIDS</h2>
                    <Bids inCart={false} items = {this.state.bids.filter(b => !b.inCart)} onDelete = {this.removeBid}></Bids>
                    <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                </div>
            </React.Fragment>
        );
    }
    removeBid = (bid) => {
        stompClient.send("/app/market/bid/deleteBid", {}, JSON.stringify({"bid_id":bid.id, "store_id" : bid.store}));
    }
    handlePurchase = () => {
        stompClient.send("/app/market/cart/getCart", {}, JSON.stringify({}));
    };


    getSum = () => {
        return this.state.baskets.map(b => b.items.
            map(i => i.price * i.amount).reduce((x,y)=> x+y,0)).reduce((x,y)=> x+y,0)
            + this.getBidsInCart().map(bid => bid.bidPrice).reduce((x,y)=> x+y,0);
    };
    getAmount = () => {
        return ( + this.getBidsInCart().map(bid => bid.amount)
            + this.state.baskets.map(b => b.items.
        map(i => i.amount).reduce((x,y)=> x+y,0)).reduce((x,y)=> x+y,0))
             ;
    };


}

export default Cart;