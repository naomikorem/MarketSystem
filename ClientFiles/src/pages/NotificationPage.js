import React, {Component, useState} from "react";
import {connectedPromise, notifications, stompClient} from "../App";

class NotificationPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            error: "",
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    render() {
        return (
            <div>
                <h1 align="center">Your Notifications</h1>

                <div className="notification-grid-container">
                    {notifications.map((listitem, index) => (
                        <div key={index} className="notification-item">
                           <label>
                               {listitem}
                           </label>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

function wrapRender() {
    return <div>
        <NotificationPage/>
    </div>
}

export default wrapRender;
