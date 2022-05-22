import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import FormLabel from 'react-bootstrap/FormLabel'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import Checkbox from "@material-ui/core/Checkbox";
import ResultLabel from "../Components/ResultLabel";
import AddItemPopup from "../Components/AddItemPopup";
import Select from "@material-ui/core/Select";
import {categories} from "../Shared/Shared";



class EditItemPopup extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: "",
            item: props.itemDTO
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    handleClose() {
        this.props.setShow(false);
    }


    async componentDidMount() {
        await connectedPromise;

        stompClient.subscribe('/user/topic/modifyItemResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeId}));
                this.handleClose();
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        this.mounted = false;
    }

    handleChange(event) {
        if (event.target.name === "amount" && (!/^[0-9]+$/.test(event.target.value) || event.target.value.length > 8)) {
            return;
        }
        this.state.item[event.target.name] = event.target.value;
        this.setState({
            ["item"]: this.state.item //set this.state.value to the input's value
        });
    }


    handleSave() {
        stompClient.send("/app/market/modifyItem", {}, JSON.stringify({
            "storeId": this.props.storeId,
            "itemId": this.state.item.item_id,
            "name": this.state.item.product_name,
            "category": this.state.item.category,
            "price": (this.state.item.price).toString(),
            "amount": parseInt(this.state.item.amount),
            "keywords": this.state.item.keyWords.toString()
        }));
    }

    handleShow() {

        this.props.setShow(true);
    }

    render() {
        return (
            <>

                <div>
                    <Button className={"itemLabel"} onClick={this.handleShow}>
                        <div> Product: {this.props.itemDTO.product_name}</div>
                        <div> Amount: {this.props.itemDTO.amount}</div>
                        Edit product
                    </Button>
                </div>

                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Edit product</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <TextField id="outlined-basic" label="Product name" variant="standard" className={"editItemBar"}
                                   value={this.state.item.product_name} onChange={this.handleChange} name="product_name"/>

                        <Select style={{background: "#FFFFFF"}} name="category" defaultValue={"Food"} value={this.state.item.category} labelId="categoryLabel" id="category" label="Category" variant="filled" className={"selectItemBar"} onChange={this.handleChange}>
                            { categories.map((s, i) => <option key={i} value={s}>{s}</option>)}
                        </Select>

                        <TextField type="number" id="outlined-basic" label="Product price" variant="standard" className={"editItemBar"}
                                   value={this.state.item.price} onChange={this.handleChange} name="price"/>

                        <TextField type="number" id="outlined-basic" label="Product amount" variant="standard" className={"editItemBar"}
                                   value={this.state.item.amount} onChange={this.handleChange} name="amount"/>

                        <TextField id="outlined-basic" label="Keywords" variant="standard" className={"editItemBar"}
                                   value={this.state.item.keyWords} onChange={this.handleChange} name="keyWords"/>


                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Save
                        </Button>
                        <Button variant="secondary" onClick={this.handleClose}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}


function wrapRender(props) {
    let [show, setShow] = useState(false);
    let storeId = props.storeId
    let itemDTO = props.itemDTO
    return <>
        <EditItemPopup storeId={storeId} itemDTO={itemDTO} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;