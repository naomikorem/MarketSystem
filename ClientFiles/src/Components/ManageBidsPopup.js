import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import ResultLabel from "../Components/ResultLabel";
import EditBidPopup from "./EditBidPopup";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;


class ManageBidsPopup extends Component {

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
        stompClient.subscribe('/user/topic/bid/getBidsResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                this.state.items = res.object;
                this.setState({[this.state.items]: this.state.items});
            }
        });
        stompClient.subscribe('/user/topic/bid/approveAllBidsResult', (r) => {
                const res = JSON.parse(r["body"]);
                this.state.error = res.errorMessage
                this.setState({[this.state.error]: this.state.error});
                if (!res.errorMessage) {
                    stompClient.send("/app/market/bid/getBids", {}, JSON.stringify({"store_id" : this.props.storeId}));
                }
            });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/bid/getBidsResult');
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }
    handleAcceptAll = () => {
        stompClient.send("/app/market/bid/approveAllBids", {}, JSON.stringify({"store_id": this.props.storeId }));
    };


    handleSave() {
        handleClose()
    }

    handleShow() {
        stompClient.send("/app/market/bid/getBids", {}, JSON.stringify({"store_id" : this.props.storeId}));
        setShow(true);
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Manage Bids
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Manage Bids</Modal.Title>
                    </Modal.Header>
                    <Modal.Body style={{borderBottom: "lightGray 1px solid"}}>
                        <Modal.Title>Unapproved Bids</Modal.Title>
                        {this.state.items.filter(b => !b.isApproved).map((bid, index) => (
                            <EditBidPopup  key={"unapproved" + index} bid={bid}/>
                        ))}
                    </Modal.Body>
                    <Modal.Body style={{borderBottom: "lightGray 1px solid"}}>
                        <Modal.Title>Approved Bids</Modal.Title>
                        {this.state.items.filter(b => (b.isApproved && !b.inCart)).map((bid, index) => (
                            <EditBidPopup  key={"approved" + index} bid={bid}/>
                        ))}
                    </Modal.Body>
                    <Modal.Body>
                        <Modal.Title>Bids In Costumer Cart</Modal.Title>
                        {this.state.items.filter(b => b.inCart).map((bid, index) => (
                            <EditBidPopup  key={"incart" + index} bid={bid}/>
                        ))}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="primary" onClick={this.handleAcceptAll}>accept all</Button>
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
        <ManageBidsPopup storeId={storeId}/>
    </>
}

export default wrapRender;