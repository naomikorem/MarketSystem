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


function deleteManager(toRemove, storeId) {
    stompClient.send("/app/market/removeManager", {}, JSON.stringify({
        "toRemove": toRemove,
        "storeId": storeId
    }));
}

function addManager(manager, storeId) {
    stompClient.send("/app/market/addManager", {}, JSON.stringify({
        "manager": manager,
        "storeId": storeId
    }));
}


class ManagersPopup extends Component {

    constructor() {
        super();

        this.state = {
            error: "",
            managerName: "",
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
        stompClient.subscribe('/user/topic/removeManagerResult', this.handleResponse);
        stompClient.subscribe('/user/topic/addManagerResult', this.handleResponse);
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/removeManagerResult');
        stompClient.unsubscribe('/user/topic/addManagerResult');
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
                        Manage managers
                    </Button>
                    <Modal show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>Manage managers</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {
                                this.props.managers.map((manager, index) =>
                                    <div key={index}>
                                        <label className={"managerName"}> {manager}</label>
                                        <Button className={"deleteManagerButton"} onClick={() =>
                                            deleteManager(manager, this.props.storeId)
                                        }>Delete</Button>
                                    </div>
                                )
                            }
                            <TextField id="outlined-basic" label="Manager name" variant="filled"
                                       className={"managerNameBar"}
                                       value={this.state.managerName} onChange={this.handleChange} name="managerName"/>
                            <Button className={"addManagerButton"}
                                    onClick={() => addManager(this.state.managerName, this.props.storeId)}>Add</Button>
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

    let managers = props.managers
    let storeId = props.storeId
    return <>
        <ManagersPopup managers={managers} storeId={storeId}/>
    </>
}

export default wrapRender;