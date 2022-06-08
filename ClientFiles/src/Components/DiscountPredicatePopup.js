import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import ResultLabel from "../Components/ResultLabel";
import Select from "@material-ui/core/Select";
import EditItemPopup from "./EditItemPopup";
import {categories} from "../Shared/Shared";
import TextField from "@material-ui/core/TextField";

const discountTypes = ["Item", "Category", "Minimal Basket Price"];
const predicateTypes = ["AND", "OR", "XOR"];


class DiscountPredicatePopup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            discount: props.discount,
            storeId: props.storeId,
            type: "Item",
            discountProperties: {"discountType": predicateTypes[0], minPrice: "0", "category": "Food"},
            items: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.formByType = this.formByType.bind(this);
        this.handleDiscountPropertiesChange = this.handleDiscountPropertiesChange.bind(this);
    }


    async componentDidMount() {
        stompClient.subscribe('/user/topic/getStoreItemsResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.items = response.object
                this.setState({[this.state.items]: this.state.items});
            }
        });
        stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeId}));
        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoreItemsResult');
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    handleDiscountPropertiesChange(event) {
        this.state.discountProperties[event.target.name] = event.target.value;
        this.setState({
            [this.state.discountProperties] : this.state.discountProperties
        });
        console.log(this.state.discountProperties);
    }


    handleSave() {
        switch (this.state.type) {
            case "Item":
                if (this.state.discountProperties.itemId == null) {
                    this.state.error = "Please select an item to put the discount on";
                    this.setState({
                        [this.state.error] : this.state.error
                    });
                    return;
                }
                stompClient.send("/app/market/addItemPredicateToDiscount", {}, JSON.stringify({
                    "storeId": this.state.storeId,
                    "discountId": this.state.discount.id,
                    "discountType": this.state.discountProperties.discountType,
                    "itemId": this.state.discountProperties.itemId,
                }));
                break;
            case "Category":
                if (this.state.discountProperties.category == null) {
                    this.state.error = "Please select a category to put the discount on";
                    this.setState({
                        [this.state.error]: this.state.error
                    });
                    return;
                }
                stompClient.send("/app/market/addCategoryPredicateToDiscount", {}, JSON.stringify({
                    "storeId": this.state.storeId,
                    "discountId": this.state.discount.id,
                    "discountType": this.state.discountProperties.discountType,
                    "category": this.state.discountProperties.category,
                }));
                break;
            case "Minimal Basket Price":
                if (this.state.discountProperties.minPrice == null) {
                    this.state.error = "Please select a category to put the discount on";
                    this.setState({
                        [this.state.error]: this.state.error
                    });
                    return;
                }
                stompClient.send("/app/market/addBasketRequirementPredicateToDiscount", {}, JSON.stringify({
                    "storeId": this.state.storeId,
                    "discountId": this.state.discount.id,
                    "discountType": this.state.discountProperties.discountType,
                    "minPrice": this.state.discountProperties.minPrice,
                }));
                break;
        }
        this.handleClose();
    }

    handleClose() {
        this.props.setShow(false);
    }

    handleShow() {
        this.props.setShow(true);
    }


    formByType() {
        switch (this.state.type) {
            case "Item":
                return <>
                    <select style={{background: "#FFFFFF"}} name="itemId" defaultValue={0} value={this.state.discountProperties.itemId} variant="filled" className={"selectItemBar"} onChange={this.handleDiscountPropertiesChange}>
                        <option value={0} disabled>Choose an item ...</option>
                        { this.state.items.map((item, i) => <option key={i} value={item.item_id}>{`${item.item_id}: ${item.product_name}`}</option>)}
                    </select>
                </>
            case "Category":
                return <>
                    <select style={{background: "#FFFFFF"}} name="category" defaultValue={"0"} value={this.state.discountProperties.category} variant="filled" className={"selectItemBar"} onChange={this.handleDiscountPropertiesChange}>
                        <option value="0" disabled>Choose a category ...</option>
                        { categories.map((s, i) => <option key={i} value={s}>{s}</option>)}
                    </select>
                </>
            case "Minimal Basket Price":
                return <>
                    <TextField type="number" id="outlined-basic" label="Minimal price" variant="standard"
                               className={"editItemBar"}
                               value={this.state.discountProperties.minPrice} onChange={this.handleDiscountPropertiesChange} name="minPrice"/>
                </>
            default:
                return "";
        }
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} >
                    Add discount predicate
                </Button>
                <Modal show={this.props.show}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add discount predicate</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        <select style={{background: "#FFFFFF"}} name="type" value={this.state.type} variant="filled" className={"selectItemBar"} onChange={this.handleChange}>
                            { discountTypes.map((s, i) => <option key={i} value={s}>{s}</option>)}
                        </select>

                        {
                            this.formByType()
                        }
                        <select style={{background: "#FFFFFF"}} name="discountType" value={this.state.discountProperties.discountType} variant="filled" className={"selectItemBar"} onChange={this.handleDiscountPropertiesChange}>
                            { predicateTypes.map((s, i) => <option key={i} value={s}>{s}</option>)}
                        </select>

                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Add
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
        <DiscountPredicatePopup storeId={storeId} discount={discount} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;