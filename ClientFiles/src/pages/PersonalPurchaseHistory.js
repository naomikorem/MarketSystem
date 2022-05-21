import React, {Component, useState} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";
import Modal from "../Components/Modal";
import RateItemPopup from "../Components/RateItemPopup";

function PersonalPurchaseHistoryItem(props) {

    let [popupOpen, setOpenPopup] = useState(false);
    const history_item = props.item;

    let onContinue = (item_id, rate) => {
        stompClient.send("/app/market/SetItemRate", {}, JSON.stringify({"store_id": history_item.store_id,"item_id": item_id, "rate" : rate}));
    };

    return (
        <div>
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
            <div className="store-grid-container">
                <button className="history-button">Review Item</button>
                <button className="history-button" onClick={() => {setOpenPopup(true)}}>Rate</button>
                <button className="history-button">Complain</button>
            </div>

            {popupOpen && <RateItemPopup
                id={history_item.item_id}
                product_name = {history_item.product_name}
                setOpenPopup={setOpenPopup}
                onContinue={onContinue}/>}

        </article>

    </div>
    );
}

export default class PersonalPurchaseHistory extends Component{

    constructor() {
        super();
        this.state = {
            history_items: [],
            error: "",
            is_error: false
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

        stompClient.subscribe('/user/topic/SetItemRateResult', (r) => {
            const res_item = JSON.parse(r["body"]);
            const errorMsg = res_item.errorMessage;

            if (errorMsg) {
                this.state.error = errorMsg;
                this.state.is_error = true
                this.setState({[this.state.is_error]: true});
            }
            else if(res_item.object)
            {
                this.state.is_error = false
                this.setState({[this.state.is_error]: false});
                this.state.error = "Successfully added rate to item";
            }
            else
            {
                this.state.error = "Could not rate this item";
                this.state.is_error = true
                this.setState({[this.state.is_error]: true});
            }
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getPersonalHistoryResult');
    }

    render() {
        return (
            user != null ?
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">Personal Purchase History</h1>
                </div>
                <div className="store-grid-container">
                    {this.state.history_items.map((item) => (

                        <PersonalPurchaseHistoryItem
                            key={item.id}
                            item = {item}
                        />
                    ))}
                </div>
                <ResultLabel text={this.state.error} hadError={this.state.is_error}/>
            </React.Fragment> : null
        );
    }
}

