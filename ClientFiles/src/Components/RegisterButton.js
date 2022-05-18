import React, {Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user} from "../App";
import {NavLink} from "react-router-dom";


function render() {
    if (!user) {
        return (<NavLink
            to="/sign-up"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            Register
        </NavLink>);
    } else {
        return null
    }
}


export default render;