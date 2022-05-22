import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser} from "../App";
import {NavLink} from "react-router-dom";


function render() {
    if (user != null) {
        return (<NavLink
            to="/user-purchase-history"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            View User's History
        </NavLink>);
    } else {
        return null
    }
}


export default render;