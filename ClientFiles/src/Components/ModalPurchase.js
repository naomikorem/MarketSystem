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
            message: "",
            hadError: false,
            address: "",
            chosenPurchaseService: "",
            chosenSupplyService: "",
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
            "address": this.state.address,
            "p_service": this.state.chosenPurchaseService,
            "s_service": this.state.chosenSupplyService
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
                        <div className={"purchase-margin"}>
                            <TextField className="addItemField" label="Address" variant="filled"
                                       value={this.state.address} onChange={this.handleChange} name="address"/>
                        </div>
                        <div className={"purchase-margin"}>
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
                        </div>
                        <div className={"purchase-margin"}>
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