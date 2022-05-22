import React, {Component} from "react";
import {Link} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import MainPage from "./MainPage";

class SignUpForm extends Component {
    constructor() {
        super();

        this.mounted = false

        this.labelClass = "errorLabel"

        this.state = {
            email: "",
            password: "",
            username: "",
            firstname: "",
            lastname: "",
            error: ""
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        let target = event.target;
        let value = target.type === "checkbox" ? target.checked : target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/registerResult', (r) => {
            var errorMsg = JSON.parse(r["body"]).errorMessage;
            this.state.error = errorMsg ? errorMsg : "User was registered successfully";
            this.labelClass = errorMsg ? "errorLabel" : "successLabel";
            this.setState({[this.state.error]: this.state.error});
        });
        this.mounted = true;
    }

    componentWillUnmount() {
        this.mounted = false;
        stompClient.unsubscribe('/user/topic/registerResult');
    }

    handleClick = () => {
        stompClient.send("/app/market/register", {}, JSON.stringify( {
                "email": this.state.email, "username": this.state.username, "firstname": this.state.firstname,
                "lastname": this.state.lastname, "pass": this.state.password
            }));
    }

    handleSubmit(e) {
        e.preventDefault();

        console.log("The form was submitted with the following data:");
        console.log(this.state);
    }

    render() {
        return (
            <div>
            <div className="formCenter">
                <form onSubmit={this.handleSubmit} className="formFields">
                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="username">
                            Username
                        </label>
                        <input
                            type="text"
                            id="username"
                            className="formFieldInput"
                            placeholder="Enter your username"
                            name="username"
                            value={this.state.username}
                            onChange={this.handleChange}
                        />
                    </div>

                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="firstname">
                            First Name
                        </label>
                        <input
                            type="text"
                            id="firstname"
                            className="formFieldInput"
                            placeholder="Enter your first name"
                            name="firstname"
                            value={this.state.firstname}
                            onChange={this.handleChange}
                        />
                    </div>

                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="lastname">
                            Last Name
                        </label>
                        <input
                            type="text"
                            id="lastname"
                            className="formFieldInput"
                            placeholder="Enter your last name"
                            name="lastname"
                            value={this.state.lastname}
                            onChange={this.handleChange}
                        />
                    </div>

                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="password">
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            className="formFieldInput"
                            placeholder="Enter your password"
                            name="password"
                            value={this.state.password}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="email">
                            E-Mail Address
                        </label>
                        <input
                            type="email"
                            id="email"
                            className="formFieldInput"
                            placeholder="Enter your email"
                            name="email"
                            value={this.state.email}
                            onChange={this.handleChange}
                        />
                    </div>

                    {/*<div className="formField">*/}
                    {/*  <label className="formFieldCheckboxLabel">*/}
                    {/*    <input*/}
                    {/*      className="formFieldCheckbox"*/}
                    {/*      type="checkbox"*/}
                    {/*      name="hasAgreed"*/}
                    {/*      value={this.state.hasAgreed}*/}
                    {/*      onChange={this.handleChange}*/}
                    {/*    />{" "}*/}
                    {/*    I agree all statements in{" "}*/}
                    {/*    <a href="null" className="formFieldTermsLink">*/}
                    {/*      terms of service*/}
                    {/*    </a>*/}
                    {/*  </label>*/}
                    {/*</div>*/}

                    <div className="formField">
                        <button onClick={this.handleClick} className="formFieldButton">Sign Up</button>
                        {" "}
                        <Link to="/sign-in" className="formFieldLink">
                            I'm already member
                        </Link>
                        <label className={
                            this.labelClass
                        }>
                            {this.state.error}
                        </label>
                    </div>
                </form>
            </div>
            </div>);
    }
}

export default SignUpForm;
