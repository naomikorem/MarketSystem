import React, {Component} from "react";
import {newNotificationsCounter, notifications, setNewNotificationsCounter, stompClient} from "../App";

import {Badge} from "react-bootstrap";

class NotificationPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            error: "",
            newCounter: 0
        };
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        stompClient.send("/app/market/removeNotifications", {}, JSON.stringify({}));

        this.state.newCounter = newNotificationsCounter

        this.setState({
            [this.state.newCounter]: this.state.newCounter
        });
        setNewNotificationsCounter(0);
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
                <div>
                    {notifications.map((listitem, index) => (
                        <div key={index} className="notification-item">
                            {index < this.state.newCounter ? <sup><Badge bg="success">New</Badge></sup> : undefined}
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
