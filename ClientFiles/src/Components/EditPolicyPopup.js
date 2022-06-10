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
import PolicyPredicatePopup from "./PolicyPredicatePopup";


class EditPolicyPopup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            changed: false,
            policy: props.policy,
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
            if (response.object.id === this.state.policy.id) {
                this.state.policy = response.object;
                this.setState({[this.state.policy]: this.state.policy});
            }
        }
    }


    async componentDidMount() {
        stompClient.subscribe('/user/topic/changePolicyResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/getAllPurchasePolicies", {}, JSON.stringify({"storeId": this.state.storeId}));
            }
        });

        stompClient.subscribe("/user/topic/addItemPredicateToPolicyResult", this.handlePredicateResult);
        stompClient.subscribe("/user/topic/addItemNotAllowedInDatePredicateToPolicyResult", this.handlePredicateResult);

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/changePolicyResult');
        stompClient.unsubscribe("/user/topic/addItemPredicateToPolicyResult");
        stompClient.unsubscribe("/user/topic/addItemNotAllowedInDatePredicateToPolicyResult");
        this.mounted = false;
    }

    handleChange(event) {
        this.state.changed = true;
        console.log("handle change notice");
        console.log(event.target.name);
        this.state.policy[event.target.name] = event.target.value;
        this.setState({
            [this.state.policy] : this.state.policy
        });
    }


    handleSave() {

            stompClient.send("/app/market/changePolicy", {}, JSON.stringify({
                "storeId": this.state.storeId,
                "policyId": this.state.policy.id,
                "newHour": this.state.policy.hour,
                "newDate": this.state.policy.date,
            }));
            //this.state.policy.hour = this.state.hour;
            console.log("policy check");
            console.log(this.state.policy);

    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"editPolicyButton"}>
                    Edit policy
                </Button>
                <Modal show={this.props.show}>
                    <Modal.Header closeButton>
                        <Modal.Title>Edit policy</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.policy.id}
                        <div>conditions: {this.state.policy.displayString}</div>
                        <div>Date: {this.state.policy.date}</div>
                        <div>Hour: {this.state.policy.hour}</div>

                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <PolicyPredicatePopup storeId={this.state.storeId} policy={this.state.policy}/>
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
    let policy = props.policy
    return <>
        <EditPolicyPopup storeId={storeId} policy={policy} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;