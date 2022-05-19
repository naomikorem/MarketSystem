import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import ResultLabel from "../Components/ResultLabel";

class StoreToClose extends Component {

    constructor() {
        super();
        this.labelClass = "StoreToClose"

        this.state = {
            error: ""
        };
        this.handleCloseStore = this.handleCloseStore.bind(this);
    }

    async handleCloseStore(store_id) {
        await connectedPromise;
        stompClient.subscribe('/user/topic/closeStorePermanentlyStoreResult', (r) => {
            let res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage;
            this.setState({[this.state.error]: this.state.error});
        });
        stompClient.send("/app/market/closeStorePermanentlyStore", {}, JSON.stringify({"storeId" : store_id}));
    }

    render() {
        const store = this.props.store;

        return (
            <div>
                <button onClick={() => this.handleCloseStore(store.id)}>
                    <div>
                        <h3>{store.name}</h3>
                    </div>
                </button>
                <ResultLabel text={this.state.error} hadError={this.state.error != ""}/>
            </div>
        );
    }
}


export default class CloseStorePermanentlyPage extends Component{

    constructor() {
        super();
        this.state = {
            liststores: []
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.liststores = response.object
                this.setState({[this.state.liststores]: this.state.liststores});
                console.log(this.state.liststores);
                //console.log(this.state.listitems[0].product_name)
                //console.log(this.state.listitems.map((listitem) => (listitem.)));

            }
        });
        stompClient.send("/app/market/getStores", {}, JSON.stringify({}));
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1>Welcome to store {this.state.storeName}</h1>
                </div>
                <div className="store-grid-container">
                    {this.state.liststores.map((store) => (

                        <StoreToClose
                            key={store.id}
                            store = {store}
                        />
                    ))}
                </div>
            </React.Fragment>
        );
    }

    // renderItem(listitem) {
    //     return(
    //         <div key={listitem.id} className={"store-grid-item"}>
    //             {/*<Link to={`/store/${listitem.id}`} className="storeLink">*/}
    //             {/*    {listitem.name}*/}
    //             {/*</Link>*/}
    //             <label>
    //                 {listitem.product_name}
    //             </label>
    //         </div>);
}

