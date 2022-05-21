import React, { Component, useContext, useState} from "react";
import {
    stompClient,
    connectedPromise,
    UserContext,
    user,
    setUser,
    setToken,
    notifications,
    setNotifications
} from "../App";


function onLogoutButton() {
    console.log("logout");
    setUser(null);
    setToken(null);
    stompClient.send("/app/market/logout", {}, {});
    //notifications = [];
    setNotifications([]);
}

function render() {
    if (user != null) {
        return (<button className={"pageSwitcherItem"} onClick={onLogoutButton}>
            Logout
        </button>);
    } else {
        return null
    }
}


export default render;