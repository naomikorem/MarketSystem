import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser} from "../App";
import {NavLink} from "react-router-dom";


function render() {
    if (user != null) {
        return (<NavLink
            to="/close-store-permanently-admin"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            Close Store Permanently
        </NavLink>);
    } else {
        return null
    }
}


export default render;