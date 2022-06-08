import React from "react";
import ReactDOM from "react-dom";
import "./index.css";

import App, {connectedPromise, notifications, setIsAdmin, setNotifications, stompClient, notificationCounter, setNotificationCounter} from "./App";

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
        notifications.push(res.object.message);
        setNotifications(notifications);
        setNotificationCounter(notifications.length)
    });

    stompClient.subscribe('/user/topic/getNotificationsResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setNotifications(res.object.map(n => n.message));
            setNotificationCounter(notifications.length)
        }
    });
}

init()
ReactDOM.render(<App/>, document.getElementById("root"));

