import React, { Component } from "react";
import { Link } from "react-router-dom";
import {stompClient} from "../App";

class SignInForm extends Component {
  constructor() {
    super();

    this.state = {
      username: "",
      password: ""
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleClick = () => {
    stompClient.send("/app/market/login", {}, JSON.stringify({"user" : "username", "pass" : "pass"}));
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

  render() {
    return (
      <div className="formCenter">
        <form className="formFields" onSubmit={this.handleSubmit}>
          <div className="formField">
            <label className="formFieldLabel" htmlFor="username">
              Username
            </label>
            <input
              type="username"
              id="username"
              className="formFieldInput"
              placeholder="Enter your username"
              name="username"
              value={this.state.username}
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
            <button onClick={this.handleClick} className="formFieldButton">Sign In</button>{" "}
            <Link to="/" className="formFieldLink">
              Create an account
            </Link>
          </div>

          {/*<div className="socialMediaButtons">*/}
          {/*  <div className="facebookButton">*/}
          {/*    <FacebookLoginButton onClick={() => alert("Hello")} />*/}
          {/*  </div>*/}

          {/*  <div className="instagramButton">*/}
          {/*    <InstagramLoginButton onClick={() => alert("Hello")} />*/}
          {/*  </div>*/}
          {/*</div>*/}
        </form>
      </div>
    );
  }
}

export default SignInForm;
