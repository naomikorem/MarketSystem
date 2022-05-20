import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";
import {Link} from "react-router-dom";

class StorePurchaseHistoryItem extends Component {

    constructor() {
        super();


    }

    render() {
        const history_item = this.props.item;

        return (
            <article className={"items-grid"}>
                <div>
                    <h3>{history_item.product_name}</h3>
                    <p>
                        Bought amount: {history_item.amount}<br/>
                        Price Per Unit: {history_item.price_per_unit} <br/>
                        Store id: {history_item.store_id} <br/>
                        Purchase Date: {new Date(history_item.date).toLocaleDateString("en-GB")}
                    </p>
                </div>
            </article>
        );
    }
}

export default class StorePurchaseHistory extends Component{

    store = props.store;

    constructor() {
        super();
        this.state = {
            history_items: [],
            error: ""
        };

    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreHistoryResult', (r) => {
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
        stompClient.send("/app/market/getStoreHistory", {}, JSON.stringify({"storeId" : store.id}));
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoreHistoryResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">Store's Purchase History</h1>
                </div>
                <div className="formCenter">
                    <h2 className="center-text">Please choose store to view it's purchase history</h2>
                </div>

                <div className="store-grid-container">
                    {this.state.history_items.map((item) => (

                        <StorePurchaseHistoryItem
                            key={item.id}
                            item = {item}
                        />
                    ))}
                </div>
                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </React.Fragment>
        );
    }
}

