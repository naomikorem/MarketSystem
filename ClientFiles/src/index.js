import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App, {connectedPromise, setIsAdmin, stompClient} from "./App";

async function listenIsAdmin() {
    await connectedPromise;
    stompClient.unsubscribe('/user/topic/isAdminResult')
    stompClient.subscribe('/user/topic/isAdminResult', (r) => {
        let res = JSON.parse(r["body"]);
        if (!res.errorMessage) {
            setIsAdmin(res.object);
        }
    });

    stompClient.unsubscribe('/user/topic/notificationResult')
    stompClient.subscribe('/user/topic/notificationResult', (r) => {
        let res = JSON.parse(r["body"]);
        //console.log(r);
    });
}
listenIsAdmin()
ReactDOM.render(<App />, document.getElementById("root"));
