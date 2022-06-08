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
import DiscountPredicatePopup from "./DiscountPredicatePopup";


class EditDiscountPopup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            changed: false,
            discount: props.discount,
            storeId: props.storeId,
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handlePredicateResult = this.handlePredicateResult.bind(this);
    }

    handleClose() {
        this.props.setShow(false);
    }

    handleShow() {
        this.props.setShow(true);
    }

    handlePredicateResult(r) {
        let response = JSON.parse(r["body"]);
        if (!response.errorMessage) {
            if (response.object.id === this.state.discount.id) {
                this.state.discount = response.object;
                this.setState({[this.state.discount]: this.state.discount});
            }
        }
    }


    async componentDidMount() {
        stompClient.subscribe('/user/topic/changeDiscountPercentageResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getAllDiscounts", {}, JSON.stringify({"storeId": this.state.storeId}));
            }
        });

        stompClient.subscribe("/user/topic/addItemPredicateToDiscountResult", this.handlePredicateResult);
        stompClient.subscribe("/user/topic/addCategoryPredicateToDiscountResult", this.handlePredicateResult);
        stompClient.subscribe("/user/topic/addBasketRequirementPredicateToDiscountResult", this.handlePredicateResult);
        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/changeDiscountPercentageResult');
        stompClient.unsubscribe("/user/topic/addItemPredicateToDiscountResult");
        stompClient.unsubscribe("/user/topic/addCategoryPredicateToDiscountResult");
        stompClient.unsubscribe("/user/topic/addBasketRequirementPredicateToDiscountResult");
        this.mounted = false;
    }

    handleChange(event) {
        this.state.changed = true;
        this.state.discount[event.target.name] = event.target.value;
        this.setState({
            [this.state.discount] : this.state.discount
        });
    }


    handleSave() {
        if (this.state.changed) {
            stompClient.send("/app/market/changeDiscountPercentage", {}, JSON.stringify({
                "storeId": this.state.storeId,
                "discountId": this.state.discount.id,
                "newPercentage": this.state.discount.percentage,
            }));
            console.log("here!")
            console.log(this.state.discount);
        }
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"editDiscountButton"}>
                    Edit discount
                </Button>
                <Modal show={this.props.show}>
                    <Modal.Header closeButton>
                        <Modal.Title>Edit discount</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.discount.id}
                        <div>conditions: {this.state.discount.displayString}</div>
                        <TextField type="number" id="outlined-basic" label="Discount percentage" variant="standard"
                                   className={"editItemBar"}
                                   value={this.state.discount.percentage} onChange={this.handleChange} name="percentage"/>


                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <DiscountPredicatePopup storeId={this.state.storeId} discount={this.state.discount}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Apply
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
    let discount = props.discount
    return <>
        <EditDiscountPopup storeId={storeId} discount={discount} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;