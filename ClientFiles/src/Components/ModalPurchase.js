import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import Checkbox from "@material-ui/core/Checkbox";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
import ResultLabel from "../Components/ResultLabel";
import {categories} from "../Shared/Shared";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;

class ModalPurchase extends Component {

    constructor() {
        super();
        this.state = {
            message: "",
            hadError: false,
            address: "",
            purchaseService: "",
            supplyService: "",
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
            "p_service": this.state.purchaseService,
            "s_service": this.state.supplyService
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
                        <TextField className="addItemField" label="Address" variant="filled"
                                   value={this.state.address} onChange={this.handleChange} name="address"/>
                        <TextField className="addItemField" label="Purchase Service" variant="filled"
                                   value={this.state.purchaseService} onChange={this.handleChange} name="purchaseService"/>
                        <TextField className="addItemField" label="Supply Service" variant="filled"
                                   value={this.state.supplyService} onChange={this.handleChange} name="supplyService"/>
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