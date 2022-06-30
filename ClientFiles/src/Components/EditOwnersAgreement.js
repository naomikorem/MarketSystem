import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise, user} from "../App";
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



class EditOwnersAgreement extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: "",
            agreement: this.props.agreement
        };
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    handleClose() {
        this.props.setShow(false);
    }


    async componentDidMount() {
        await connectedPromise;

        stompClient.subscribe('/user/topic/oa/approveOwnerAgreementResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/oa/getOwnerAgreements", {}, JSON.stringify({"store_id" : this.props.storeId}));
                stompClient.send("/app/market/getStoreInfo", {}, JSON.stringify({"store_id" : this.props.storeId}));
                this.handleClose();
            }
        });
        stompClient.subscribe('/user/topic/oa/deleteOwnerAgreementResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/oa/getOwnerAgreements", {}, JSON.stringify({"store_id" : this.props.storeId}));
                this.handleClose();
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/oa/approveOwnerAgreementResult');
        stompClient.unsubscribe('/user/topic/oa/deleteOwnerAgreementResult');
        this.mounted = false;
    }

    handleShow  () {
        this.props.setShow(true);
    }
    handleAccept = () => {
        stompClient.send("/app/market/oa/approveOwnerAgreement", {}, JSON.stringify({
            "store_id": this.props.storeId,
            "owner_name": this.state.agreement.owner
        }));
    };
    handleReject = () => {
        stompClient.send("/app/market/oa/deleteOwnerAgreement", {}, JSON.stringify({
            "store_id": this.props.storeId,
            "owner_name": this.state.agreement.owner
        }));

    };
    render() {
        return (
            <>
                <div>
                    <Button className={"itemLabel"} onClick={this.handleShow}>
                        <p>Costumer: {this.props.agreement.owner} </p>
                    </Button>
                </div>

                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Owners Agreement</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                         <p>
                            not approved by:<br />
                            {this.state.agreement.notApprovedOwners.map(name => <> {name}<br /></>)}
                        </p>
                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        {this.state.agreement.notApprovedOwners.includes(user.userName) ?
                            <div>
                                <Button variant="primary" onClick={this.handleAccept}>
                                    Accept
                                </Button>
                                <Button variant="primary" onClick={this.handleReject}>
                                    Reject
                                </Button>
                            </div>:<></>
                        }
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
    let agreement = props.agreement;
    return <>
        <EditOwnersAgreement storeId={props.storeId} agreement={agreement} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;