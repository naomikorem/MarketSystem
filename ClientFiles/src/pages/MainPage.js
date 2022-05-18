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
                className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
            >
                Sign In
            </NavLink>
            <NavLink
                exact
                to="/sign-up"
                className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}

            >
                Sign Up
            </NavLink>
        </div>

        <div className="formTitle">
            <NavLink
                to="/sign-in"
                className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}

            >
                Sign In
            </NavLink>{" "}
            or{" "}
            <NavLink
                exact
                to="/sign-up"
                className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}

            >
                Sign Up
            </NavLink>
        </div></div>

    );
    }
}

export default MainPage;
