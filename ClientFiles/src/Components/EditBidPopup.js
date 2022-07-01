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



class EditBidPopup extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: "",
            bid: this.props.bid
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    handleClose() {
        this.props.setShow(false);
    }


    async componentDidMount() {
        await connectedPromise;

        stompClient.subscribe('/user/topic/bid/approveBidResult', (r) => {
            console.log("a b r")
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                this.state.bid.approvedManagers.push(user.userName);
                this.setState({[this.state.bid.approvedManagers]: this.state.bid.approvedManagers});
                stompClient.send("/app/market/bid/getBids", {}, JSON.stringify({"store_id" : this.props.bid.store}));
                //this.handleClose();
            }
        });
        stompClient.subscribe('/user/topic/bid/updateBidResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/bid/getBids", {}, JSON.stringify({"store_id" : this.props.bid.store}));
                this.handleClose();
            }
        });
        stompClient.subscribe('/user/topic/bid/deleteBidResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/bid/getBids", {}, JSON.stringify({"store_id" : this.props.bid.store}));
                this.handleClose();
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/bid/approveBidResult');
        stompClient.unsubscribe('/user/topic/bid/updateBidResult');
        stompClient.unsubscribe('/user/topic/bid/deleteBidResult');
        this.mounted = false;
    }

    handleChange(event) {
        this.state.bid[event.target.name] = event.target.value;
        this.setState({
            bid: this.state.bid //set this.state.value to the input's value
        });
        console.log(this.state.bid);
    }


    handleSave() {
        stompClient.send("/app/market/bid/updateBid", {}, JSON.stringify({
            "store_id": this.props.bid.store, "bid_id": this.state.bid.id, "bid_price": (this.state.bid.bidPrice)
        }));
    }

    handleShow() {
        if(!this.props.bid.isApproved)
            this.props.setShow(true);
    }
    handleAccept = () => {
        stompClient.send("/app/market/bid/approveBid", {}, JSON.stringify({
            "store_id": this.props.bid.store,
            "bid_id": this.state.bid.id
        }));
    };
    handleReject = () => {
        stompClient.send("/app/market/bid/deleteBid", {}, JSON.stringify({
            "store_id": this.props.bid.store,
            "bid_id": this.state.bid.id
        }));

    };
    render() {
        return (
            <>
                <div>
                    <Button className={"itemLabel"} onClick={this.handleShow}>
                        <p>Costumer: {this.props.bid.costumerName} Product: {this.props.bid.item} bid: {this.props.bid.bidPrice}</p>
                    </Button>
                </div>

                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Process Bid</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <TextField type="number" id="outlined-basic" label="Change Bidding" variant="standard" className={"editItemBar"}
                                   value={this.state.bid.bidPrice} onChange={this.handleChange} name="bidPrice"/>
                        <p>
                            approved by:<br />
                            {this.state.bid.approvedManagers.map(name => <> {name}<br /></>)}
                        </p>
                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        {!this.state.bid.approvedManagers.includes(user.userName) ?
                            <div>
                                <Button variant="primary" onClick={this.handleSave}>
                                    Update
                                </Button>
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
    let bid = props.bid
    return <>
        <EditBidPopup bid={bid} show={show} setShow={setShow}/>
    </>
}

export default wrapRender;