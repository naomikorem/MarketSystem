import React from "react";
import ReactDOM from "react-dom";
import "./index.css";

import App, {
    connectedPromise,
    notifications,
    setIsAdmin,
    setNotifications,
    setToken,
    setUser,
    stompClient
} from "./App";

async function init() {
    await connectedPromise;
    stompClient.unsubscribe('/user/topic/isAdminResult')
    stompClient.subscribe('/user/topic/isAdminResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setIsAdmin(res.object);
        }
    });

    stompClient.subscribe('/user/topic/notificationResult', (r) => {
        let res = JSON.parse(r["body"]);
        notifications.push(res.object.message);
    });

    stompClient.subscribe('/user/topic/getNotificationsResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setNotifications(res.object.map(n => n.message));
        }
    });
    stompClient.subscribe('/user/topic/loginByTokenResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            sessionStorage.setItem('user', JSON.stringify(res.object))
            if (res.object.userName) {
                setUser(res.object)
            } else {
                setUser(null)
            }
            stompClient.send("/app/market/isAdmin", {}, JSON.stringify());
        } else {
            setUser(null);
            setToken(null);
        }
    });
}

init()
ReactDOM.render(<App />, document.getElementById("root"));

