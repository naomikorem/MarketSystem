import {stompClient, connectedPromise, user} from "../App";
import React, { Component } from "react";
import ObjectsGrid from "../Components/ObjectsGrid";
import ResultLabel from "../Components/ResultLabel";


class ManageStores extends Component {
    constructor(props) {
        super(props);
        this.state = {
            open_store_items: [],
            closed_store_items: [],
            error: "",
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getUsersStoresResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (res.errorMessage == null)
            {
                console.log(res.object)

                this.state.error = "";

                this.state.open_store_items = res.object.filter(s => s.isOpen);
                this.setState({[this.state.open_store_items]: this.state.open_store_items});
                this.state.closed_store_items = res.object.filter(s => !s.isOpen && !s.permanentlyClosed);
                this.setState({[this.state.closed_store_items]: this.state.closed_store_items});
            }
            else
            {
                this.state.error = res.errorMessage;
            }
            this.setState({[this.state.error]: this.state.error});
        });
        stompClient.send("/app/market/getUsersStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <div>
                <h1 className="center-text">Hello {user ? user.userName : "Guest"}, Choose a store to manage</h1>
                <div>
                    <h4>Your Open Stores</h4>
                    <div className="store-grid-container">
                        <ObjectsGrid listitems={this.state.open_store_items} link={"edit-store"}/>
                    </div>
                </div>
                <div>
                    <h4>Your Closed Stores</h4>
                    <div className="store-grid-container">
                        <ObjectsGrid listitems={this.state.closed_store_items} link={"edit-store"}/>

                    </div>
                </div>
                <ResultLabel text={this.state.error} hadError={true}/>
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
