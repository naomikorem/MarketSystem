import React, {Component} from "react";
import {stompClient, connectedPromise} from "../App";
import ResultLabel from "../Components/ResultLabel";

class RemoveUserSubscription extends Component {

    constructor() {
        super();

        this.label = "errorLabel";

        this.state = {
            username_to_unsubscribe: "",
            message: "",
            hadError: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleApply = this.handleApply.bind(this);
    }

    handleApply() {
        stompClient.send("/app/market/removeSubscription", {}, JSON.stringify({"username" : this.state.username_to_unsubscribe}));
    }

    handleChange(event) {
        let target = event.target;
        let value = target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/removeSubscriptionResult', (r) => {
            let response = JSON.parse(r["body"]);
            if(response.errorMessage || !response.object)
            {
                console.log(response.errorMessage);
                this.state.message = "Could not remove the user's subscription";
                this.state.hadError = true;
                this.setState({[this.state.hadError]: this.state.hadError});
            }
            else {
                this.state.message = "Removed the user's subscription";
            }
            this.setState({[this.state.message]: this.state.message});
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/removeSubscriptionResult');
    }

    render() {
        return (
            <div className="formCenter">
                <h1>Please enter name of subscribed user</h1>
                <div className="formField">
                    <label className="formFieldLabel">
                        Username to remove his subscription
                    </label>
                    <input
                        type="username_to_unsubscribe"
                        id="username_to_unsubscribe"
                        className="formFieldInput"
                        placeholder="Enter name of subscribed user"
                        name="username_to_unsubscribe"
                        value={this.state.username_to_unsubscribe}
                        onChange={this.handleChange}
                    />
                </div>
                <div className="formField">
                    <button onClick={this.handleApply} className="formFieldButton">Remove</button>
                </div>

                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </div>
        );
    }
}

export default RemoveUserSubscription;