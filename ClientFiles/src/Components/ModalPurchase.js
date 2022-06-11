import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import ResultLabel from "../Components/ResultLabel";

import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';


let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;

class ModalPurchase extends Component {
    constructor() {
        super();
        this.state = {
            // payment details
            card_number: "",
            month: "",
            year: "",
            holder: "",
            ccv: "",
            id: "",

            // supply details
            address: "",
            city: "",
            country: "",
            zip: "",

            // choose services
            chosenPurchaseService: "",
            chosenSupplyService: "",

            message: "",
            hadError: false,
            purchaseServiceNames: [],
            supplyServiceNames: []
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/purchaseResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.message  = res.errorMessage ? res.errorMessage : "Purchase completed successfully!";
            this.state.hadError = res.errorMessage !== null;
            this.setState({[this.state.error]: this.state.error, [this.state.message]: this.state.message});

            if (!res.errorMessage) {
                this.props.onPurchase();
                handleClose();
            }
        });

        /*const updateServicesNames = (names_list, r) => {
            const res = JSON.parse(r["body"]);

            this.state.message  = res.errorMessage ? res.errorMessage : "";
            this.state.hadError = res.errorMessage !== null;
            this.setState({[this.state.error]: this.state.error, [this.state.message]: this.state.message});

            if (!res.errorMessage) {
                names_list = res.object;
                this.setState({[names_list]: names_list});
                console.log("done");
                console.log(names_list);
                console.log(this.state.purchaseServiceNames);
                console.log(this.state.supplyServiceNames);
            }
        }*/

        stompClient.subscribe('/user/topic/getAllExternalPurchaseServicesNamesResult', (r) => {
            /*updateServicesNames(this.state.purchaseServiceNames, r);

            console.log(this.state.purchaseServiceNames);*/

            const res = JSON.parse(r["body"]);

            this.state.message  = res.errorMessage ? res.errorMessage : "";
            this.state.hadError = res.errorMessage !== null;
            this.setState({[this.state.error]: this.state.error, [this.state.message]: this.state.message});

            if (!res.errorMessage) {
                this.state.purchaseServiceNames = res.object;
                this.setState({[this.state.purchaseServiceNames]: this.state.purchaseServiceNames});
                //
                // if (this.state.purchaseServiceNames.length > 0)
                // {
                //     this.state.chosenPurchaseService = this.state.purchaseServiceNames[0];
                //     this.setState({[this.state.chosenPurchaseService]: this.state.chosenPurchaseService});
                //
                // }
            }
        });

        stompClient.subscribe('/user/topic/getAllExternalSupplyServicesNamesResult', (r) => {
            /*updateServicesNames(this.state.supplyServiceNames, r);

            console.log(this.state.supplyServiceNames);*/

            const res = JSON.parse(r["body"]);

            this.state.message  = res.errorMessage ? res.errorMessage : "";
            this.state.hadError = res.errorMessage !== null;
            this.setState({[this.state.error]: this.state.error, [this.state.message]: this.state.message});

            if (!res.errorMessage) {
                this.state.supplyServiceNames = res.object;
                this.setState({[this.state.supplyServiceNames]: this.state.supplyServiceNames});

                // if (this.state.supplyServiceNames.length > 0)
                // {
                //     this.state.chosenSupplyService = this.state.supplyServiceNames[0];
                //     this.setState({[this.state.chosenSupplyService]: this.state.chosenSupplyService});
                // }
            }
        });

        stompClient.send("/app/market/getAllExternalPurchaseServicesNames", {}, {});
        stompClient.send("/app/market/getAllExternalSupplyServicesNames", {}, {});

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/purchaseResult')
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }


    handleSave() {
        stompClient.send("/app/market/purchase", {}, JSON.stringify({
            // payment details
            "card_number": this.state.card_number,
            "month": this.state.month,
            "year": this.state.year,
            "holder": this.state.holder,
            "ccv": this.state.ccv,
            "id": this.state.id,

            // supply details
            "name": this.state.holder,
            "address": this.state.address,
            "city": this.state.city,
            "country": this.state.country,
            "zip": this.state.zip,

            // choose services
            "paymentServiceName": this.state.chosenPurchaseService,
            "supplyServiceName": this.state.chosenSupplyService
        }));
    }

    handleShow() {
        setShow(true);
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow}>
                    Purchase
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Purchase</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="purchase-margin">
                            <h6>Payment Details</h6>
                            <TextField className="addItemField" label="Card Number" variant="filled"
                                       value={this.state.card_number} onChange={this.handleChange} name="card_number"/>
                            <TextField className="addItemField" label="Month" variant="filled"
                                       value={this.state.month} onChange={this.handleChange} name="month"/>
                            <TextField className="addItemField" label="Year" variant="filled"
                                       value={this.state.year} onChange={this.handleChange} name="year"/>
                            <TextField className="addItemField" label="Holder" variant="filled"
                                       value={this.state.holder} onChange={this.handleChange} name="holder"/>
                            <TextField className="addItemField" label="ccv" variant="filled"
                                       value={this.state.ccv} onChange={this.handleChange} name="ccv"/>
                            <TextField className="addItemField" label="ID" variant="filled"
                                       value={this.state.id} onChange={this.handleChange} name="id"/>
                        </div>
                        <div className="purchase-margin">
                            <h6>Supply Details</h6>
                            <TextField className="addItemField" label="Address" variant="filled"
                                       value={this.state.address} onChange={this.handleChange} name="address"/>
                            <TextField className="addItemField" label="City" variant="filled"
                                       value={this.state.city} onChange={this.handleChange} name="city"/>
                            <TextField className="addItemField" label="Country" variant="filled"
                                       value={this.state.country} onChange={this.handleChange} name="country"/>
                            <TextField className="addItemField" label="Zip" variant="filled"
                                        value={this.state.zip} onChange={this.handleChange} name="zip"/>
                        </div>
                        <div className="purchase-margin">
                            <h6>Choose Services</h6>
                            <Box sx={{ minWidth: 120 }}>
                                <FormControl fullWidth variant="filled">
                                    <InputLabel id="demo-simple-select-label">Choose Purchase Service</InputLabel>
                                    <Select label="Choose Purchase Service"
                                            name="chosenPurchaseService"
                                            value={this.state.chosenPurchaseService}
                                            variant="filled"
                                            className={"selectItemBarPurchase"}
                                            onChange={this.handleChange}>
                                        {this.state.purchaseServiceNames.map((s, i) => <MenuItem key={i} value={s}>{s}</MenuItem>)}
                                    </Select>
                                </FormControl>
                            </Box>
                            <Box sx={{ minWidth: 120 }}>
                                <FormControl fullWidth variant="filled">
                                    <InputLabel id="demo-simple-select-label">Choose Supply Service</InputLabel>
                                    <Select label="Choose Supply Service"
                                            name="chosenSupplyService"
                                            value={this.state.chosenSupplyService}
                                            variant="filled"
                                            className={"selectItemBarPurchase"}
                                            onChange={this.handleChange}>
                                        {this.state.supplyServiceNames.map((s, i) => <MenuItem key={i} value={s}>{s}</MenuItem>)}
                                    </Select>
                                </FormControl>
                            </Box>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Purchase
                        </Button>
                        <Button variant="secondary" onClick={handleClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}


function wrapRender(props) {
    [show, setShow] = useState(false);
    handleClose = () => setShow(false);
    return <>
        <ModalPurchase onPurchase={props.onPurchase}/>
    </>
}

export default wrapRender;