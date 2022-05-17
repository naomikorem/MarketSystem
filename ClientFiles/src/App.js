import React, { Component } from "react";
import { HashRouter as Router, Route, NavLink } from "react-router-dom";
import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import GuestHomePage from "./pages/GuestHomePage";
import MainPage from "./pages/MainPage";

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

            {/*<div className="pageSwitcher">*/}
              {/*<NavLink*/}
              {/*  to="/sign-in"*/}
              {/*  activeClassName="pageSwitcherItem-active"*/}
              {/*  className="pageSwitcherItem"*/}
              {/*>*/}
              {/*  Sign In*/}
              {/*</NavLink>*/}
              {/*<NavLink*/}
              {/*  exact*/}
              {/*  to="/sign-up"*/}
              {/*  activeClassName="pageSwitcherItem-active"*/}
              {/*  className="pageSwitcherItem"*/}
              {/*>*/}
              {/*  Sign Up*/}
              {/*</NavLink>*/}
            {/*</div>*/}

            {/*<div className="formTitle">*/}
            {/*  <NavLink*/}
            {/*    to="/sign-in"*/}
            {/*    activeClassName="formTitleLink-active"*/}
            {/*    className="formTitleLink"*/}
            {/*  >*/}
            {/*    Sign In*/}
            {/*  </NavLink>{" "}*/}
            {/*  or{" "}*/}
            {/*  <NavLink*/}
            {/*    exact*/}
            {/*    to="/sign-up"*/}
            {/*    activeClassName="formTitleLink-active"*/}
            {/*    className="formTitleLink"*/}
            {/*  >*/}
            {/*    Sign Up*/}
            {/*  </NavLink>*/}
            {/*</div>*/}

            <Route path="/sign-up" component={SignUpForm} />
            <Route path="/sign-in" component={SignInForm} />
            <Route exact path="/main-page" component={MainPage} />
            <Route path="/guest-home-page" component={GuestHomePage} />
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
