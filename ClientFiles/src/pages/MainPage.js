import React, { Component } from "react";
import { NavLink } from "react-router-dom";
import {stompClient, connectedPromise} from "../App";

class MainPage extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div>
            <div className="pageSwitcher">
            <NavLink
                to="/sign-in"
                className="pageSwitcherItem-active"
                className="pageSwitcherItem"
            >
                Sign In
            </NavLink>
            <NavLink
                exact
                to="/sign-up"
                className="pageSwitcherItem-active"
                className="pageSwitcherItem"
            >
                Sign Up
            </NavLink>
        </div>

        <div className="formTitle">
            <NavLink
                to="/sign-in"
                className="formTitleLink-active"
                className="formTitleLink"
            >
                Sign In
            </NavLink>{" "}
            or{" "}
            <NavLink
                exact
                to="/sign-up"
                className="formTitleLink-active"
                className="formTitleLink"
            >
                Sign Up
            </NavLink>
        </div></div>

    );
    }
}

export default MainPage;
