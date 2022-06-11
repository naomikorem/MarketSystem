import React, {Component} from "react";
import {stompClient, connectedPromise} from "../App";
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

const serviceTypes = ["Supply service", "Purchase Service"];

class AddService extends Component {

    constructor() {
        super();

        this.label = "errorLabel";

        this.state = {
            serviceName: "",
            serviceUrl: "",
            serviceType: "",
            message: "",
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleApply = this.handleApply.bind(this);
    }

    handleApply() {
        switch(this.state.serviceType)
        {
            case serviceTypes[0]:
                stompClient.send("/app/market/addExternalSupplyService", {}, JSON.stringify({"name" : this.state.serviceName, "address" : this.state.serviceUrl}));
                break;

            case serviceTypes[1]:
                stompClient.send("/app/market/addExternalPurchaseService", {}, JSON.stringify({"name" : this.state.serviceName, "address" : this.state.serviceUrl}));
                break;

            default:
                break;
        }
    }

    handleChange(event) {
        let target = event.target;
        let value = target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    handleServiceChange(event) {
        let target = event.target;
        let value = target.value;

        this.state.serviceType = value;
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/addExternalServiceResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (response.errorMessage){

                this.state.message = JSON.parse(r["body"]).errorMessage;
                this.label = "errorLabel";
                this.setState({[this.state.message]: this.state.message});
            }
            else
            {
                this.state.message = "The new service added successfully";
                this.label = "successLabel";
                this.setState({[this.state.message]: this.state.message});
            }
        });
    }

    render() {
        const textFieldStyling = {
            "& label": {
                color: "#616e7f",
            },
            "&.Mui-selected": { color: "#ffffff"}
        };
        return (
            <div className="formCenter">
                <div className="formField">
                    <label className="formFieldLabel" htmlFor="service_name">
                        External Service Name
                    </label>
                    <input
                        type="serviceName"
                        id="serviceName"
                        className="formFieldInput"
                        placeholder="Enter the new service name"
                        name="serviceName"
                        value={this.state.serviceName}
                        onChange={this.handleChange}
                    />
                </div>

                <div className="formField">
                    <label className="formFieldLabel" htmlFor="service_url">
                        External Service URL
                    </label>
                    <input
                        type="serviceUrl"
                        id="serviceUrl"
                        className="formFieldInput"
                        placeholder="Enter the new service url"
                        name="serviceUrl"
                        value={this.state.serviceUrl}
                        onChange={this.handleChange}
                    />
                </div>

                {/*<div className="formField">*/}
                {/*    <Box sx={{ minWidth: 120 }}>*/}
                {/*        /!*<FormControl fullWidth sx={{...textFieldStyling }}>*!/*/}
                {/*            <FormControl fullWidth>*/}
                {/*            <InputLabel  className="addExternalServiceType" id="demo-simple-select-label">Choose Service Type</InputLabel>*/}
                {/*            <Select*/}
                {/*                sx={{bgcolor:'red'}}*/}
                {/*                options={this.state.supplyServiceNames}*/}
                {/*                    label="Choose Service Type"*/}
                {/*                    name="serviceType"*/}
                {/*                    value={this.state.serviceType}*/}
                {/*                    variant="filled"*/}
                {/*                    className="formFieldInput"*/}
                {/*                    onChange={this.handleChange}>*/}
                {/*                {serviceTypes.map((s, i) => <MenuItem key={i} value={s}>{s}</MenuItem>)}*/}
                {/*            </Select>*/}
                {/*        </FormControl>*/}
                {/*    </Box>*/}
                {/*</div>*/}

                <Box sx={{ width: '130%' }}>
                    <FormControl fullWidth variant="filled">
                        <InputLabel id="demo-simple-select-label">Choose Supply Service</InputLabel>
                            <Select style={{background: "#FFFFFF"}}
                                    name="serviceType"
                                    value={this.state.serviceType}
                                    variant="filled" className={"selectItemBar"}
                                    onChange={this.handleChange}>
                                { serviceTypes.map((s, i) => <MenuItem key={i} value={s}>{s}</MenuItem>)}
                            </Select>
                    </FormControl>
                </Box>

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

export default AddService;