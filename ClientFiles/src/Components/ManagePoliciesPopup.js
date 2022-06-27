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
import AddPolicyPopup from "./AddPolicyPopup";
import EditPolicyPopup from "./EditPolicyPopup";

function removePolicy(storeId, policyId) {
    stompClient.send("/app/market/removePolicy", {}, JSON.stringify({
        "storeId": storeId,
        "policyId": policyId,
    }));
}

class ManagePoliciesPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            policies: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }


    async componentDidMount() {

        stompClient.subscribe('/user/topic/getAllPurchasePoliciesResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                this.state.policies = res.object;
                this.setState({[this.state.policies]: this.state.policies});
            }
        });

        stompClient.subscribe('/user/topic/removePolicyResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getAllPurchasePolicies", {}, JSON.stringify({"storeId": this.props.storeId}));
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getAllPurchasePoliciesResult');
        stompClient.unsubscribe('/user/topic/removePolicyResult');
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }


    handleShow() {
        stompClient.send("/app/market/getAllPurchasePolicies", {}, JSON.stringify({"storeId": this.props.storeId}));
        this.props.setShow(true);
    }

    handleClose() {
        this.props.setShow(false);
    }


    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Manage policies
                </Button>
                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Manage policies</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        {this.state.policies.map((listitem, index) => (

                            <div key={index} className={"policyDescription"}>
                                <label> Policy id: {listitem.id}</label>
                                <div><Button className={"deletePolicyButton"} onClick={() =>
                                    removePolicy(this.props.storeId, listitem.id)
                                }>Remove</Button></div>
                                <EditPolicyPopup key={listitem.id} storeId={this.props.storeId} policy={{id: listitem.id, hour: listitem.hour, date: listitem.date, displayString: listitem.displayString}}/>
                            </div>
                        ))}

                        <AddPolicyPopup storeId={this.props.storeId}/>

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
        <ManagePoliciesPopup storeId={storeId} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;