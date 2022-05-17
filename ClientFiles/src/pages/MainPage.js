import React, { Component } from "react";
import { HashRouter as Router, Route, NavLink } from "react-router-dom";
import SignUpForm from "./SignUpForm";
import SignInForm from "./SignInForm";
import GuestHomePage from "./GuestHomePage";

class MainPage extends Component {
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
                                to="/sign-up"
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
                                to="/sign-up"
                                activeClassName="formTitleLink-active"
                                className="formTitleLink"
                            >
                                Sign Up
                            </NavLink>
                        </div>
                    </div>
                </div>
            </Router>
        );
    }
}

export default MainPage;
