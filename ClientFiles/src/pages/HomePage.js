// import React, { Component } from "react";
// import { HashRouter as Router, Route, NavLink } from "react-router-dom";
// import {stompClient, connectedPromise} from "../App";
//
// class HomePage extends Component {
//     constructor() {
//         super();
//     }
//
//     async componentDidMount() {
//         await connectedPromise;
//         stompClient.subscribe('/market/getStores', (r) => {
//             console.log(JSON.parse(r["body"]));
//             //this.setState({[this.state.error]: this.state.error});
//         });
//     }
//
//     render() {
//         const components = [new ListItem("abc"), new ListItem("abc2"), new ListItem("abc3")] // references to components
//         return (
//             //<div><h1>Hello</h1></div>
//         <div>
//             {components.map((comp, i) => React.createElement(comp, { key: i })}
//         </div>
//         );
//     }
// }
//
// export default HomePage;

import {stompClient, connectedPromise} from "../App";

import React, { Component } from "react";

class HomePage extends Component {
    constructor() {
        super();
        stompClient.send("/app/market/getStores", {}, {});
    }

    state = {
        listitems: [],
        error: ""
    };


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (res.errorMessage == null)
            {
                this.state.listitems = res.object.map(store => store.name);
                this.setState({[this.state.listitems]: this.state.listitems});
            }
            else
            {
                this.state.error = res.errorMessage;
                this.setState({[this.state.error]: this.state.error});
            }
        });
    }

    render() {
        return (
            <React.Fragment>
                <h1>Choose store</h1>
                <ul className="list-group">
                    {this.state.listitems.map(listitem => (
                        <li>
                            {listitem}
                        </li>
                    ))}
                </ul>
                <label className="errorLabel">
                    {this.state.error}
                </label>
            </React.Fragment>
        );
    }
}

export default HomePage;