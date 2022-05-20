import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";

class PersonalPurchaseHistoryItem extends Component {

    constructor() {
        super();
    }

    render() {
        const history_item = this.props.item;

        return (
            <div className="formCenterHistoryItem">
                <div className="formField">
                    <h3>{history_item.product_name}</h3>
                </div>
                <div className="formField">
                    <label className="formFieldProfileLabel">
                        Bought amount:
                    </label>
                    <label className="formFieldProfileInputLabel">
                        {history_item.amount}
                    </label>
                </div>
                <div className="formField">
                    <label className="formFieldProfileLabel">
                        Price Per Unit:
                    </label>
                    <label className="formFieldProfileInputLabel">
                        {history_item.price_per_unit}
                    </label>
                </div>
                <div className="formField">
                    <label className="formFieldProfileLabel">
                        Store id:
                    </label>
                    <label className="formFieldProfileInputLabel">
                        {history_item.store_id}
                    </label>
                </div>
                <div className="formField">
                    <label className="formFieldProfileLabel">
                        Purchase Date:
                    </label>
                    <label className="formFieldProfileInputLabel">
                        {history_item.date}
                    </label>
                </div>
                <div className="formField">
                    <button className="formFieldButton">Review Item</button>
                </div>
                <div className="formField">
                    <button className="formFieldButton">Rate Item And Store</button>
                </div>
                <div className="formField">
                    <button className="formFieldButton">Complain</button>
                </div>
            </div>
        );
    }
}

export default class PersonalPurchaseHistory extends Component{

    constructor() {
        super();
        this.state = {
            history_items: [],
            error: ""
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getPersonalHistoryResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.errorMessage) {
                this.state.history_items = response.object.items
                this.setState({[this.state.history_items]: this.state.history_items});
                console.log(this.state.history_items);

            }
            else
            {
                this.state.error = response.errorMessage;
                this.setState({[this.state.error]: this.state.error});
            }
        });
        console.log(user);
        stompClient.send("/app/market/getPersonalHistory", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getPersonalHistoryResult');
    }

    render() {
        return (
            user != null ?
            <React.Fragment>
                <div className="formCenter">
                    <h1>Personal Purchase History</h1>
                </div>
                <div className="grid-container">
                    {this.state.history_items.map((item) => (

                        <PersonalPurchaseHistoryItem
                            key={item.id}
                            item = {item}
                        />
                    ))}
                </div>
                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </React.Fragment> : null
        );
    }
}

