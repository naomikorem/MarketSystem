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

const policyTypes = ["Hour for buying item until", "Date you are not allowed to buy the item at"];
const predicateTypes = ["AND", "OR"];


class PolicyPredicatePopup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            policy: props.policy,
            storeId: props.storeId,
            type: "Item",
            date:"noun",
            hour:"noun",
            policyProperties: {"policyType": ""},
            items: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.formByType = this.formByType.bind(this);
        this.handlePolicyPurchaseChange = this.handlePolicyPurchaseChange.bind(this);
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
        console.log("changer");
        console.log(event.target.value);
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
        this.state.policy.date = this.state.date;
        this.state.policy.hour = this.state.hour;
    }


    handlePolicyPurchaseChange(event) {
        this.state.policyProperties[event.target.name] = event.target.value;
        this.setState({
            [this.state.policyProperties] : this.state.policyProperties
        });

        this.state.policy.date = this.state.date;
        this.state.policy.hour = this.state.hour;
        console.log("info:");
        console.log(this.state.policyProperties);
    }


    handleSave() {
        switch (this.state.type) {
            case "Hour for buying item until":
                if (this.state.policyProperties.itemId == null) {
                    this.state.error = "Please select an item to put the policy on";
                    this.setState({
                        [this.state.error] : this.state.error
                    });
                    return;
                }
                stompClient.send("/app/market/addItemPredicateToPolicy", {}, JSON.stringify({
                    "storeId": this.state.storeId,
                    "policyId": this.state.policy.id,
                    "policyType": this.state.policyProperties.policyType,
                    "itemId": this.state.policyProperties.itemId,
                    "hour" : this.state.hour,
                }));
                this.state.policy.date = this.state.date;
                this.state.policy.hour = this.state.hour;
                break;

            case "Date you are not allowed to buy the item at":
                if (this.state.policyProperties.itemId == null) {
                    this.state.error = "Please select an item to put the policy on";
                    this.setState({
                        [this.state.error] : this.state.error
                    });
                    return;
                }
                stompClient.send("/app/market/addItemNotAllowedInDatePredicateToPolicy", {}, JSON.stringify({
                    "storeId": this.state.storeId,
                    "policyId": this.state.policy.id,
                    "policyType": this.state.policyProperties.policyType,
                    "itemId": this.state.policyProperties.itemId,
                    "date": this.state.date,
                }));
                this.state.policy.date = this.state.date;
                this.state.policy.hour = this.state.hour;
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
        //console.log("formByType");
        //console.log(this.state.type);
        switch (this.state.type) {
            case "Hour for buying item until":
                return <>
                    <select style={{background: "#FFFFFF"}} name="itemId" defaultValue={0} value={this.state.policyProperties.itemId} variant="filled" className={"selectItemBar"} onChange={this.handlePolicyPurchaseChange}>
                        <option value={0} disabled>Choose an item ...</option>
                        { this.state.items.map((item, i) => <option key={i} value={item.item_id}>{`${item.item_id}: ${item.product_name}`}</option>)}
                    </select>

                    <TextField type="number" id="outlined-basic" label="policy hour" variant="standard"
                                   className={"editItemBar"}
                                   value={this.state.hour} onChange={this.handleChange} name="hour"/>

                </>
            case "Date you are not allowed to buy the item at":
                return <>
                    <select style={{background: "#FFFFFF"}} name="itemId" defaultValue={0} value={this.state.policyProperties.itemId} variant="filled" className={"selectItemBar"} onChange={this.handlePolicyPurchaseChange}>
                        <option value={0} disabled>Choose an item ...</option>
                        { this.state.items.map((item, i) => <option key={i} value={item.item_id}>{`${item.item_id}: ${item.product_name}`}</option>)}
                    </select>

                    <TextField type="date" id="outlined-basic" label="policy date" variant="standard"
                               className={"editItemBar"}
                               value={this.state.date} onChange={this.handleChange} name="date"/>
                </>
            default:
                return "";
        }
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} >
                    Add policy predicate
                </Button>
                <Modal show={this.props.show}>
                    <Modal.Header closeButton>
                        <Modal.Title>Add policy predicate</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        <select style={{background: "#FFFFFF"}} name="type" value={this.state.type} variant="filled" className={"selectItemBar"} onChange={this.handleChange}>
                            { policyTypes.map((s, i) => <option key={i} value={s}>{s}</option>)}
                        </select>

                        {
                            this.formByType()
                        }
                        <select style={{background: "#FFFFFF"}} name="policyType" value={this.state.policyProperties.policyType} variant="filled" className={"selectItemBar"} onChange={this.handlePolicyPurchaseChange}>
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
    let policy = props.policy
    return <>
        <PolicyPredicatePopup storeId={storeId} policy={policy} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;

