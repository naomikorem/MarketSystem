import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";

export default class GetSubscriberInfoPage extends Component{

    constructor() {
        super();
        this.state = {
            username_to_see: "",
            firstName: "",
            lastName: "",
            email: "",
            error: ""
        };


        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleClick = () => {
        stompClient.send("/app/market/getSubscriberInfo", {}, JSON.stringify({"username" : this.state.username_to_see}));
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
        stompClient.subscribe('/user/topic/getSubscriberInfoResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.errorMessage) {
                this.state.firstName = response.object.firstName
                this.setState({[this.state.firstName]: this.state.firstName});

                this.state.lastName = "Last Name: " + response.object.lastName
                this.setState({[this.state.lastName]: this.state.lastName});

                this.state.email = "Email Address: " + response.object.email
                this.setState({[this.state.email]: this.state.email});
            }
            else
            {
                this.state.error = response.errorMessage;
                this.setState({[this.state.error]: this.state.error});

                this.state.firstName = ""
                this.setState({[this.state.firstName]: this.state.firstName});

                this.state.lastName = ""
                this.setState({[this.state.lastName]: this.state.lastName});

                this.state.email = ""
                this.setState({[this.state.email]: this.state.email});
            }
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getSubscriberInfoResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">View subscriber's profile information</h1>
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

                </form>

                {this.state.firstName === "" || this.state.error !== "" ? null : <div>
                    <div className="formCenter">
                    <h2 className="center-text">Here is the information of @{this.state.username_to_see}</h2>
                    </div>
                    <form className="formFields">
                        <div className="formField">
                            <label className="formFieldProfileLabel">
                                First Name:
                            </label>
                            <label className="formFieldProfileInputLabel">
                                {this.state.firstName}
                            </label>
                        </div>
                        <div className="formField">
                            <label className="formFieldProfileLabel">
                                Last Name:
                            </label>
                            <label className="formFieldProfileInputLabel">
                                {this.state.lastName}
                            </label>
                        </div>
                        <div className="formField">
                            <label className="formFieldProfileLabel">
                                Email:
                            </label>
                            <label className="formFieldProfileInputLabel">
                                {this.state.email}
                            </label>
                        </div>

                    </form>

                </div>}
                <ResultLabel text={this.state.error} hadError={this.state.error !== ""}/>

            </React.Fragment>
        );
    }
}

