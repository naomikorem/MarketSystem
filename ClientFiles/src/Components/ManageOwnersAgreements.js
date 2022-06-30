import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import ResultLabel from "../Components/ResultLabel";
import EditBidPopup from "./EditBidPopup";
import ManageOwnersAgreements from "./ManageOwnersAgreements";
import EditOwnersAgreement from "./EditOwnersAgreement";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;


class ManageOwnersAgreement extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            items: []
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/oa/getOwnerAgreementsResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                this.state.items = res.object;
                this.setState({[this.state.items]: this.state.items});
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/oa/getOwnerAgreementsResult');
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
        stompClient.send("/app/market/oa/getOwnerAgreements", {}, JSON.stringify({"store_id" : this.props.storeId}));
        setShow(true);
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Manage Owners Agreement
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Manage Bids</Modal.Title>
                    </Modal.Header>
                    <Modal.Body style={{borderBottom: "lightGray 1px solid"}}>
                        <Modal.Title>Unapproved Bids</Modal.Title>
                        {this.state.items.filter(b => !b.isApproved).map((agreement, index) => (
                            <EditOwnersAgreement storeId={this.props.storeId} key={"unapproved" + index} agreement={agreement}/>
                        ))}
                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
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
        <ManageOwnersAgreement storeId={storeId}/>
    </>
}

export default wrapRender;