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
import AddDiscountPopup from "./AddDiscountPopup";
import EditDiscountPopup from "./EditDiscountPopup";

function removeDiscount(storeId, discountId) {
    stompClient.send("/app/market/removeDiscount", {}, JSON.stringify({
        "storeId": storeId,
        "discountId": discountId,
    }));
}

class ManageDiscountsPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            discounts: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }


    async componentDidMount() {
        stompClient.subscribe('/user/topic/getAllDiscountsResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                this.state.discounts = res.object;
                this.setState({[this.state.discounts]: this.state.discounts});
            }
        });

        stompClient.subscribe('/user/topic/removeDiscountResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getAllDiscounts", {}, JSON.stringify({"storeId": this.props.storeId}));
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getAllDiscountsResult');
        stompClient.unsubscribe('/user/topic/removeDiscountResult');
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }


    handleShow() {
        stompClient.send("/app/market/getAllDiscounts", {}, JSON.stringify({"storeId": this.props.storeId}));
        this.props.setShow(true);
    }

    handleClose() {
        this.props.setShow(false);
    }


    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Manage discounts
                </Button>
                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Manage discounts</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        {this.state.discounts.map((listitem, index) => (

                            <div key={index} className={"discountDescription"}>
                                <label> Discount id: {listitem.id} Discount percentage: {listitem.percentage}</label>
                                <div><Button className={"deleteDiscountButton"} onClick={() =>
                                    removeDiscount(this.props.storeId, listitem.id)
                                }>Remove</Button></div>
                                <EditDiscountPopup key={listitem.id} storeId={this.props.storeId} discount={{id: listitem.id, percentage: listitem.percentage, displayString: listitem.displayString}}/>
                            </div>
                        ))}

                        <AddDiscountPopup storeId={this.props.storeId}/>

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
    return <>
        <ManageDiscountsPopup storeId={storeId} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;