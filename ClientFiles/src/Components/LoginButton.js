import React, {Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user} from "../App";
import {NavLink} from "react-router-dom";


function render()
{
    if (!user) {
        return (<NavLink
            to="/sign-in"
            className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
        >
            Login
        </NavLink>);
    } else {
        return null
    }
}


export default render;