import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import ResultLabel from "../Components/ResultLabel";




class EditBidCostumerPopup extends Component {

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

        stompClient.subscribe('/user/topic/bid/updateBidResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.error = res.errorMessage;
            this.setState({[this.state.error]: this.state.error});
            if (!res.errorMessage) {
                stompClient.send("/app/market/bid/getUserBids", {}, JSON.stringify({}));
                this.handleClose();
            }
        });

        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/bid/updateBidResult');
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
        this.props.setShow(true);
    };

    render() {
        return (
            <>
                <div>
                    <Button onClick={this.handleShow}>Update</Button>
                </div>
                <Modal show={this.props.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Process Bid</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <TextField type="number" id="outlined-basic" label="Change Bidding" variant="standard" className={"editItemBar"}
                                   value={this.state.bid.bidPrice} onChange={this.handleChange} name="bidPrice"/>
                    </Modal.Body>
                    <Modal.Footer>
                        <ResultLabel text={this.state.error} hadError={this.state.error != null}/>
                        <Button variant="primary" onClick={this.handleSave}>
                            Update
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
    let bid = props.bid
    return <>
        <EditBidCostumerPopup bid={bid} show={show} setShow={setShow}/>
    </>
}
export default wrapRender;