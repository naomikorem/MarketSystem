import React, { Component } from "react";
import { HashRouter as Router, Route, NavLink } from "react-router-dom";
import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import MainPage from "./pages/MainPage";
import HomePage from "./pages/HomePage";

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

const socket = new SockJS("http://localhost:8080/market");
export const stompClient = Stomp.over(socket);
export const connectedPromise = new Promise(resolve => {
  stompClient.connect({}, function (frame) {
    resolve(true)
  }, function (error) {
    alert(error);
  });
})


class App extends Component {
  render() {
    return (
      <Router basename="/react-auth-ui/">
        <div className="App">
          <div className="appAside" />
          <div className="appForm">
            <Route exact path="/" component={SignInForm} />
            <Route path="/sign-in" component={SignInForm} />
            <Route path="/sign-up" component={SignUpForm} />
            <Route path="/home" component={HomePage} />
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
