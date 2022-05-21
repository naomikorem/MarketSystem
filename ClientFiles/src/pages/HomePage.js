import {stompClient, connectedPromise, user} from "../App";
import React, { Component } from "react";
import ObjectsGrid from "../Components/ObjectsGrid";
import TextField from "@material-ui/core/TextField";


class HomePage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            listitems: [],
            error: "",
            searchValue : "",
        };
        this.handleChange = this.handleChange.bind(this);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getOpenStoresResult', (r) => {
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
        stompClient.send("/app/market/getOpenStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getOpenStoresResult');
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    render() {
        return (
            <div>
            <h1 align="center">Hello {user ? user.userName : "Guest"}, Choose a store to view</h1>

                <TextField id="outlined-basic" label="Search a store" variant="filled" className={"searchBar"} value={this.state.seachValue} onChange={this.handleChange} name="searchValue"/>

                <div className="store-grid-container">
                    <ObjectsGrid listitems={this.state.listitems.filter(s => {
                return s.name.includes(this.state.searchValue) && s.isOpen && !s.permanentlyClosed;
            })} link={"store"}/>
        </div>
            </div>

        );
    }
}

function wrapRender() {
    return <div>
        <HomePage/>
    </div>
}

export default wrapRender;
