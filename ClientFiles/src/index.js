import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App, {connectedPromise, notifications, setIsAdmin, stompClient} from "./App";

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
        console.log(res.object);
        console.log(res.object.message);
        notifications.push(res.object.message);
    });
}

init()
ReactDOM.render(<App />, document.getElementById("root"));
