import React, { Component } from "react";
import { HashRouter as Router, Route, NavLink } from "react-router-dom";
import {stompClient, connectedPromise} from "../App";

class HomePage extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div><h1>Hello</h1></div>
        );
    }
}

export default HomePage;
