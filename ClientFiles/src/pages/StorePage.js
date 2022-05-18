import React, {Component} from "react";
import { useParams } from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
class StorePage extends Component {

    constructor() {
        super();

        this.state = {
            storeName: "",
        };



    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreInfoResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.storeName = response.object.name
                this.setState({[this.state.storeName]: this.state.storeName});
            }
        });
        stompClient.send("/app/market/getStoreInfo", {}, JSON.stringify({"id" : this.props.storeid}));

    }

    render() {
    return (
      <div className="formCenter">
        <h1>{this.state.storeName}</h1>
      </div>
    );
  }
}

function wrapRender() {
    let {storeid } = useParams();
    return <div>
        <StorePage storeid={storeid}/>
    </div>
}

export default wrapRender;