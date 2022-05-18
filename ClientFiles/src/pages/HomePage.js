import {stompClient, connectedPromise} from "../App";
import React, { Component } from "react";
import {Link, NavLink, useNavigate} from "react-router-dom";

//import { useNavigate } from "react-router-dom";
//import {useNavigate} from "react-router-dom";
//
// // v6 examples

// Navigate to new URL
//navigate("/keyhole")

class HomePage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            listitems: [],
            error: "",
            user: JSON.parse(sessionStorage.getItem('user'))
        };
    }



    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
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
        stompClient.send("/app/market/getStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <div>
            <h1>Hello {this.state.user ? this.state.user.userName : "Guest"}, Choose a store to view</h1>
        <div className="store-grid-container">
            {this.state.listitems.map((listitem, index) => (
                <div key={index} className={"store-grid-item"}>
                    <Link to={`/store/${listitem.id}`} className="storeLink">
                        {listitem.name}
                    </Link>
                </div>
            ))}
        </div>
            </div>

        );
    }
}

function wrapRender() {
    let navigate = useNavigate();
    return <div>
        <HomePage navigate={navigate}/>
    </div>
}

export default wrapRender;
