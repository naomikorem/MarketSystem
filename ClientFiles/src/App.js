import logo from './logo.svg';
import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import './App.css';

var socket = new SockJS("http://localhost:8080/market");
var stompClient = Stomp.over(socket);
stompClient.connect();

function activateLasers() {
    stompClient.send('/app/market/enter/aaa', {}, {}, 0);
    stompClient.send('/app/market/login', {}, JSON.stringify({'user': "user", 'pass': "pass"}), 0)
    console.log("here");
}

function App() {

  return (

      <Router basename="/react-auth-ui/">
          <div className="App">
              <div className="appAside" />
              <div className="appForm">
                  <div className="pageSwitcher">
                      <NavLink
                          to="/sign-in"
                          activeClassName="pageSwitcherItem-active"
                          className="pageSwitcherItem"
                      >
                          Sign In
                      </NavLink>
                      <NavLink
                          exact
                          to="/"
                          activeClassName="pageSwitcherItem-active"
                          className="pageSwitcherItem"
                      >
                          Sign Up
                      </NavLink>
                  </div>

                  <div className="formTitle">
                      <NavLink
                          to="/sign-in"
                          activeClassName="formTitleLink-active"
                          className="formTitleLink"
                      >
                          Sign In
                      </NavLink>{" "}
                      or{" "}
                      <NavLink
                          exact
                          to="/"
                          activeClassName="formTitleLink-active"
                          className="formTitleLink"
                      >
                          Sign Up
                      </NavLink>
                  </div>

                  <Route exact path="/" component={SignUpForm} />
                  <Route path="/sign-in" component={SignInForm} />
              </div>
          </div>
      </Router>
  );
}


/*import React, { Component } from "react";
import { HashRouter as Router, Route, NavLink } from "react-router-dom";
import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";

import "./App.css";

class App extends Component {
    render() {
        return (
            <Router basename="/react-auth-ui/">
                <div className="App">
                    <div className="appAside" />
                    <div className="appForm">
                        <div className="pageSwitcher">
                            <NavLink
                                to="/sign-in"
                                activeClassName="pageSwitcherItem-active"
                                className="pageSwitcherItem"
                            >
                                Sign In
                            </NavLink>
                            <NavLink
                                exact
                                to="/"
                                activeClassName="pageSwitcherItem-active"
                                className="pageSwitcherItem"
                            >
                                Sign Up
                            </NavLink>
                        </div>

                        <div className="formTitle">
                            <NavLink
                                to="/sign-in"
                                activeClassName="formTitleLink-active"
                                className="formTitleLink"
                            >
                                Sign In
                            </NavLink>{" "}
                            or{" "}
                            <NavLink
                                exact
                                to="/"
                                activeClassName="formTitleLink-active"
                                className="formTitleLink"
                            >
                                Sign Up
                            </NavLink>
                        </div>

                        <Route exact path="/" component={SignUpForm} />
                        <Route path="/sign-in" component={SignInForm} />
                    </div>
                </div>
            </Router>
        );
    }
}*/

export default App;
