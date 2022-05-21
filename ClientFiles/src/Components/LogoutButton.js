import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser, setToken} from "../App";


function onLogoutButton() {
    setUser(null);
    setToken(null);
    stompClient.send("/app/market/logout", {}, {});
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