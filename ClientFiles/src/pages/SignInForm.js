import React, {Component, useState} from "react";
import { Link, useNavigate } from "react-router-dom";
import {stompClient, connectedPromise, UserContext, setUser, setToken, notifications, user} from "../App";
import MainPage from "./MainPage";


class SignInForm extends Component {
  constructor() {
    super();

    this.mounted = false

    this.state = {
      username: "",
      password: "",
      error: ""
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    await connectedPromise;
    console.log("signin");
    console.log(user);
    console.log(notifications);
    stompClient.subscribe('/user/topic/loginResult', (r) => {
        let res = JSON.parse(r["body"]);
        this.state.error = res.errorMessage;
        this.setState({[this.state.error]: this.state.error});
        if (!res.errorMessage) {
          sessionStorage.setItem('user', JSON.stringify(res.object))
          setUser(res.object)
          stompClient.send('/app/market/getToken', {}, JSON.stringify({}));
          stompClient.send("/app/market/isAdmin", {}, JSON.stringify({}));
          stompClient.send("/app/market/getNotifications", {}, JSON.stringify({}));
      }
    });

    stompClient.unsubscribe('/user/topic/tokenResult');
    stompClient.subscribe('/user/topic/tokenResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
          sessionStorage.setItem('token', JSON.stringify(res.object))
          setToken(res.object)
          this.props.navigate('/home')
      }
      this.mounted = true;
    });
  }

  componentWillUnmount() {
    this.mounted = false;
    stompClient.unsubscribe('/user/topic/loginResult');
  }

  handleClick = () => {
    stompClient.send("/app/market/login", {}, JSON.stringify({"user" : this.state.username, "pass" : this.state.password}));
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

  render() { return  <div>
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
            <Link to="/home" className="formFieldButton" style={{ textDecoration: 'none' }}>
              Continue as a guest
            </Link>
            <Link to="/sign-up" className="formFieldLink">
              Create an account
            </Link>
            <label className="errorLabel">
              {this.state.error}
            </label>
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
      </div></div>
    ;
  }
}

function wrapRender() {
  let navigate = useNavigate();
  return <div>
    <SignInForm navigate={navigate}/>
  </div>
}

export default wrapRender;
