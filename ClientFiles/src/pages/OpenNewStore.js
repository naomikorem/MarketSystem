import React, {Component} from "react";
import {stompClient, connectedPromise} from "../App";

class StorePage extends Component {

    constructor() {
        super();

        this.label = "errorLabel";

        this.state = {
            storeName: "",
            message: "",
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleApply = this.handleApply.bind(this);
    }

    handleApply() {
        stompClient.send("/app/market/openNewStore", {}, JSON.stringify({"name" : this.state.storeName}));
    }

    handleChange(event) {
        let target = event.target;
        let value = target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/openNewStoreResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (response.errorMessage){
                console.log(response.errorMessage);

                this.state.message = JSON.parse(r["body"]).errorMessage;
                this.label = "errorLabel";
                this.setState({[this.state.message]: this.state.message});
            }
            else
            {
                this.state.message = "The new store opened successfully";
                this.label = "successLabel";
                this.setState({[this.state.message]: this.state.message});
            }
        });
    }

    render() {
        return (
            <div className="formCenter">
                    <div className="formField">
                        <label className="formFieldLabel" htmlFor="store_name">
                            Store Name
                        </label>
                        <input
                            type="storeName"
                            id="storeName"
                            className="formFieldInput"
                            placeholder="Enter the new store name"
                            name="storeName"
                            value={this.state.storeName}
                            onChange={this.handleChange}
                        />


                    </div>
                    <div className="formField">
                        <button onClick={this.handleApply} className="formFieldButton">Apply</button>
                    </div>

                    <div className="formField">
                        <label className={this.label}>
                            {this.state.message}
                        </label>
                    </div>
            </div>
        );
    }
}


export default StorePage;