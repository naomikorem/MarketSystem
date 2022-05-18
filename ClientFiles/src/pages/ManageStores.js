import {stompClient, connectedPromise, user} from "../App";
import React, { Component } from "react";
import {Link, NavLink, useNavigate} from "react-router-dom";
import ObjectsGrid from "../Components/ObjectsGrid";

//import { useNavigate } from "react-router-dom";
//import {useNavigate} from "react-router-dom";
//
// // v6 examples

// Navigate to new URL
//navigate("/keyhole")

class ManageStores extends Component {
    constructor(props) {
        super(props);
        this.state = {
            listitems: [],
            error: "",
        };
    }



    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getUsersStoresResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (res.errorMessage == null)
            {
                this.state.listitems = res.object;
                this.setState({[this.state.listitems]: this.state.listitems});
            }
            else
            {
                this.state.error = res.errorMessage;
                this.setState({[this.state.error]: this.state.error});
            }
        });
        stompClient.send("/app/market/getUsersStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <div>
                <h1>Hello {user ? user.userName : "Guest"}, Choose a store to manage</h1>
                <div className="store-grid-container">
                    <ObjectsGrid listitems={this.state.listitems} link={"edit-store"}/>
                </div>
            </div>

        );
    }
}

function wrapRender() {
    return <div>
        <ManageStores/>
    </div>
}

export default wrapRender;
