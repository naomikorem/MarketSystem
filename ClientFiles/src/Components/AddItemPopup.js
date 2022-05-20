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

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;

const categories = ["Food", "Clothing", "Toys", "Grooming", "Fitness"]

class AddItemPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            productName: "",
            category: "Food",
            price: "0",
            amount: "0",
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/addNewItemResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeId}));
                handleClose();
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/addNewItemResult')
        this.mounted = false;
    }

    handleChange(event) {
        if (event.target.name === "amount" && (!/^[0-9]+$/.test(event.target.value) || event.target.value.length > 8)) {
            return;
        }
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }


    handleSave() {
        stompClient.send("/app/market/addNewItem", {}, JSON.stringify({
            "storeId": this.props.storeId,
            "name": this.state.productName,
            "category": this.state.category,
            "price": this.state.price,
            "amount": this.state.amount
        }));
    }

    handleShow() {
        setShow(true);
    }


    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow}>
                    Add item
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add item</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <TextField className="addItemField" label="Product name" variant="filled"
                                   value={this.state.productName} onChange={this.handleChange} name="productName"/>

                        <Select name="category" defaultValue={"Food"} value={this.state.category} labelId="categoryLabel" id="category" label="Category" variant="filled" className={"addItemSelectField"} onChange={this.handleChange}>
                            {
                                categories.map((s, i) => <option key={i} value={s}>{s}</option>)
                            }
                        </Select>

                        <TextField className="addItemField" type="number" label="Product price" variant="filled"
                                   value={this.state.price} onChange={this.handleChange} name="price"/>
                        <TextField className="addItemField" type="number" pattern='[0-9]{0,5}' label="Product amount" variant="filled"
                                   value={this.state.amount} onChange={this.handleChange} name="amount"/>

                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Add
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
    let storeId = props.storeId
    return <>
        <AddItemPopup storeId={storeId}/>
    </>
}

export default wrapRender;