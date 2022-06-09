import React from "react";
import ReactDOM from "react-dom";
import "./index.css";

import App, {connectedPromise, notifications, setIsAdmin, setNotifications, stompClient, newNotificationsCounter, setNewNotificationsCounter} from "./App";

async function init() {
    await connectedPromise;
    stompClient.unsubscribe('/user/topic/isAdminResult')
    stompClient.subscribe('/user/topic/isAdminResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setIsAdmin(res.object);
        }
    });

    // real time
    stompClient.subscribe('/user/topic/notificationResult', (r) => {
        let res = JSON.parse(r["body"]);
        notifications.unshift(res.object.message);
        setNotifications(notifications);
        setNewNotificationsCounter(newNotificationsCounter + 1)
    });

    stompClient.subscribe('/user/topic/getNotificationsResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setNotifications(res.object.map(n => n.message));
            setNewNotificationsCounter(notifications.length)
        }
    });
}

init()
ReactDOM.render(<App/>, document.getElementById("root"));

