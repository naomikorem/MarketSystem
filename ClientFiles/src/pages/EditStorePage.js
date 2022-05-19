import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import ManagersPopup from '../Components/ManagersPopup';
import OwnersPopup from '../Components/OwnersPopup';

//import { View } from 'react-native';
//import { Text } from 'react-native-paper';

import ResultLabel from "../Components/ResultLabel";



class EditStorePage extends Component {

    constructor() {
        super();

        this.state = {
            store: null,
            error: ""
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreInfoResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.errorMessage) {
                this.state.store = response.object
                this.setState({[this.state.store]: this.state.store});
            } else {
                this.state.error = response.errorMessage
                this.setState({[this.state.error]: this.state.error});
            }
        });
        stompClient.send("/app/market/getStoreInfo", {}, JSON.stringify({"id": this.props.storeid}));
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1>{this.state.store == null ? "There is nothing to see here" : this.state.store.name}</h1>
                </div>

                {this.state.store != null ?
                <div>
                    <ManagersPopup managers={this.state.store.managers} storeId={this.state.store.id} />
                    <OwnersPopup owners={this.state.store.owners} storeId={this.state.store.id} />

                </div>


                : null}


                <ResultLabel text={this.state.error} hadError={this.state.error != ""}/>
            </React.Fragment>
        );
    }
}

function wrapRender() {

    let {storeid} = useParams();
    return <div>
        <EditStorePage storeid={storeid}/>
    </div>
}

export default wrapRender;