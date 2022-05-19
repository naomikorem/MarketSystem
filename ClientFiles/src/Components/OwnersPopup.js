import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import ResultLabel from "../Components/ResultLabel";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;
let handleShow = () => undefined;


function deleteOwner(toRemove, storeId) {
    stompClient.send("/app/market/removeOwner", {}, JSON.stringify({
        "toRemove": toRemove,
        "storeId": storeId
    }));
}

function addOwner(owner, storeId) {
    stompClient.send("/app/market/addOwner", {}, JSON.stringify({
        "owner": owner,
        "storeId": storeId
    }));
}


class OwnersPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            ownerName: "",
        };

        this.handleResponse = this.handleResponse.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleResponse(r) {
        let response = JSON.parse(r["body"]);
        if (!response.errorMessage) {
            stompClient.send("/app/market/getStoreInfo", {}, JSON.stringify({"id": this.props.storeId}));
        } else {
            this.state.error = response.errorMessage
            this.setState({[this.state.error]: this.state.error});
        }
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/removeOwnerResult', this.handleResponse);
        stompClient.subscribe('/user/topic/addOwnerResult', this.handleResponse);
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/removeOwnerResult');
        stompClient.unsubscribe('/user/topic/addOwnerResult');
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    render() {
        return (
            <>
                <div>
                    <Button variant="primary" onClick={handleShow} className={"storeEditButton"}>
                        Manage owners
                    </Button>
                    <Modal show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>Manage owners</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {
                                this.props.owners.map((owner, index) =>
                                    <div key={index}>
                                        <label className={"ownerName"}> {owner}</label>
                                        <Button className={"deleteOwnerButton"} onClick={() =>
                                            deleteOwner(owner, this.props.storeId)
                                        }>Delete</Button>
                                    </div>
                                )
                            }
                            <TextField id="outlined-basic" label="Owner name" variant="filled"
                                       className={"ownerNameBar"}
                                       value={this.state.ownerName} onChange={this.handleChange} name="ownerName"/>
                            <Button className={"addOwnerButton"}
                                    onClick={() => addOwner(this.state.ownerName, this.props.storeId)}>Add</Button>
                            <ResultLabel text={this.state.error} hadError={this.state.error != ""}/>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </div>
            </>
        );
    }
}

function wrapRender(props) {
    [show, setShow] = useState(false);
    handleClose = () => setShow(false);
    handleShow = () => setShow(true);

    let owners = props.owners
    let storeId = props.storeId
    return <>
        <OwnersPopup owners={owners} storeId={storeId}/>
    </>
}

export default wrapRender;