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
import EditItemPopup from "../Components/EditItemPopup";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;


class AddDiscountPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            percentage: "0",
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleAdd = this.handleAdd.bind(this);
    }


    async componentDidMount() {
        stompClient.subscribe('/user/topic/addDiscountResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getAllDiscounts", {}, JSON.stringify({"storeId": this.props.storeId}));
                handleClose()
            }
        });

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


    handleAdd() {
        stompClient.send("/app/market/addDiscount", {}, JSON.stringify({
            "storeId": this.props.storeId,
            "percentage": this.state.percentage
        }));
    }

    handleShow() {
        setShow(true);
    }


    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Add discount
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add discount</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        <TextField type="number" id="outlined-basic" label="Discount percentage" variant="standard"
                                   className={"editItemBar"}
                                   value={this.state.percentage} onChange={this.handleChange} name="percentage"/>


                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <Button variant="secondary" onClick={this.handleAdd}>
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
        <AddDiscountPopup storeId={storeId}/>
    </>
}

export default wrapRender;