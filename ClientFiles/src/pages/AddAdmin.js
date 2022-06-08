import React, {Component} from "react";
import {stompClient, connectedPromise} from "../App";

class AddAdmin extends Component {

    constructor() {
        super();

        this.label = "errorLabel";

        this.state = {
            adminName: "",
            message: "",
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleApply = this.handleApply.bind(this);
    }

    handleApply() {
        stompClient.send("/app/market/addAdmin", {}, JSON.stringify({"name" : this.state.adminName}));
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
        stompClient.subscribe('/user/topic/addAdminResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (response.errorMessage){
                this.state.message = response.errorMessage;
                this.label = "errorLabel";
                this.setState({[this.state.message]: this.state.message});
            }
            else
            {
                if (response.object) {
                    console.log(response);
                    this.state.message = "The new admin added successfully";
                    this.label = "successLabel";
                    this.setState({[this.state.message]: this.state.message});
                }
                else
                {
                    this.state.message = "unknown error";
                    this.label = "errorLabel";
                    this.setState({[this.state.message]: this.state.message});
                }
            }
        });
    }

    render() {
        return (
            <div className="formCenter">
                <div className="formField">
                    <label className="formFieldLabel" htmlFor="store_name">
                        Admin Name
                    </label>
                    <input
                        type="adminName"
                        id="adminName"
                        className="formFieldInput"
                        placeholder="Enter the new admin name"
                        name="adminName"
                        value={this.state.adminName}
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


export default AddAdmin;