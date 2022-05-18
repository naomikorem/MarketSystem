import {stompClient, connectedPromise} from "../App";
import React, { Component } from "react";
import {Link} from "react-router-dom";

//import { useNavigate } from "react-router-dom";
//import {useNavigate} from "react-router-dom";
//
// // v6 examples

// Navigate to new URL
//navigate("/keyhole")

class HomePage extends Component {
    constructor() {
        super();
    }

    state = {
        listitems: [],
        error: ""
    };

    handleClick = (index) => {
        console.log("click" + index);
        console.log(this.state.listitems[index]);
        // const navigate = useNavigate();
        // navigate('sign-in');
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
            <React.Fragment>
                <h1>Choose store</h1>

                <div className="store-grid-container">
                    {this.state.listitems.map((listitem, index) => (
                        <div key={index} className={"store-grid-item"}>
                        <Link to={`/store/${listitem.id}`} className="storeLink">
                            {listitem.name}
                        </Link>
                        </div>
                    ))}
                </div>

                <label className="errorLabel">
                    {this.state.error}
                </label>
            </React.Fragment>
        );
    }
}

export default HomePage;
