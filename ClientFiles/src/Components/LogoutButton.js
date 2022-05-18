import React, { Component, useContext, useState} from "react";
import {stompClient, connectedPromise, UserContext, user, setUser} from "../App";


function onLogoutButton() {
    stompClient.send("/app/market/logout", {}, {});
    setUser(undefined);
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