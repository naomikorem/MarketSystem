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



class EditItemPopup extends Component {

    constructor(props) {
        super(props);
        console.log("props")
        console.log(props)
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


        this.mounted = true;
    }

    componentWillUnmount() {
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }


    handleSave() {
        handleClose()
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
                                   value={this.state.item.product_name} onChange={this.handleChange} name="productName"/>
                        <TextField id="outlined-basic" label="Product category" variant="standard" className={"editItemBar"}
                                   value={this.state.item.category} onChange={this.handleChange} name="productCategory"/>


                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
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