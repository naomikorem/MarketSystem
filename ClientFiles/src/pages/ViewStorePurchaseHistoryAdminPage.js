import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ObjectsGrid from "../Components/ObjectsGrid";

export default class StorePurchaseHistory extends Component{

    constructor() {
        super();
        this.state = {
            stores: []
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.stores = response.object
                this.setState({[this.state.stores]: this.state.stores});
                console.log(this.state.stores);
            }
        });
        stompClient.send("/app/market/getStores", {}, JSON.stringify({}));
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">Store's Purchase History</h1>
                </div>
                <div className="formCenter">
                    <h2 className="center-text">Select store to view it's purchase history</h2>
                </div>
                <div className="store-grid-container">
                    <ObjectsGrid listitems={this.state.stores} link={"store-purchase-history"}/>
                </div>
            </React.Fragment>
        );
    }
}

