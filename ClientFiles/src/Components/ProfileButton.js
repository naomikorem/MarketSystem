import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser} from "../App";
import {NavLink} from "react-router-dom";


function render() {
    if (user != null) {
        return (<NavLink
            to="/user-profile-subscriber"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            View Profile
        </NavLink>);
    } else {
        return null
    }
}


export default render;