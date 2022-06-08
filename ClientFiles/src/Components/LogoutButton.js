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
import {useNavigate} from "react-router-dom";


function onLogoutButton(navigate) {
    sessionStorage.setItem('user', null)
    sessionStorage.setItem('token', null)
    setUser(null);
    setToken(null);
    stompClient.send("/app/market/logout", {}, {});
    setNotifications([]);
    navigate('/home');
}

function render() {
    let navigate = useNavigate();
    if (user != null) {
        return (
                <button className={"pageSwitcherItem"} onClick={() => onLogoutButton(navigate)}>
                    Logout
                </button>
             );
    } else {
        return null
    }
}


export default render;