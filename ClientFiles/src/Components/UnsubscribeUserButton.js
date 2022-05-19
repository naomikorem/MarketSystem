import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser} from "../App";
import {NavLink} from "react-router-dom";


function render() {
    if (user != null) {
        return (<NavLink
            to="/remove-user-subscription-admin"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            Unsubscribe user
        </NavLink>);
    } else {
        return null
    }
}


export default render;