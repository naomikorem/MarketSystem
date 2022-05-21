import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";
import {Link} from "react-router-dom";

class UserPurchaseHistoryItem extends Component {

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

export default class UserPurchaseHistory extends Component{

    constructor() {
        super();
        this.state = {
            username_to_see: "",
            history_items: [],
            error: ""
        };


        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleClick = () => {
        stompClient.send("/app/market/getUserHistory", {}, JSON.stringify({"username" : this.state.username_to_see}));
    }

    handleChange(event) {
        let target = event.target;
        let value = target.type === "checkbox" ? target.checked : target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        console.log("The form was submitted with the following data:");
        console.log(this.state);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getUserHistoryResult', (r) => {
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

                this.state.history_items = [];
                this.setState({[this.state.history_items]: this.state.history_items});
            }
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getUserHistoryResult');
    }

    render() {
        return (
                <React.Fragment>
                    <div className="formCenter">
                        <h1 className="center-text">User's Purchase History</h1>
                    </div>
                    <form className="formFields" onSubmit={this.handleSubmit}>
                        <div className="formField">
                            <label className="formFieldUserHistoryLabel" htmlFor="username">
                                Enter name of subscribed user:
                            </label>
                            <input
                                type="username_to_see"
                                id="username_to_see"
                                className="formFieldInput"
                                placeholder="Enter name of subscribed user"
                                name="username_to_see"
                                value={this.state.username_to_see}
                                onChange={this.handleChange}
                            />
                        </div>

                        <div className="formField">
                            <button onClick={this.handleClick} className="formFieldButton">OK</button>{" "}
                        </div>
                        <ResultLabel text={this.state.error} hadError={this.state.error != ""}/>
                    </form>


                    <div className="store-grid-container">
                        {this.state.history_items.map((item) => (

                            <UserPurchaseHistoryItem
                                key={item.id}
                                item = {item}
                            />
                        ))}
                    </div>
                </React.Fragment>
        );
    }
}

