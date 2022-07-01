import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css';
import TextField from "@material-ui/core/TextField";
import Checkbox from "@material-ui/core/Checkbox";
import ResultLabel from "../Components/ResultLabel";

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;

class ManagerPermissionPopup extends Component {

    constructor() {
        super();

        this.state = {
            resultMessage: "",
            hadError: false,
            permission: {
                givenBy: "",
                permissionMask: 0,
            },
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleCheckboxChanged = this.handleCheckboxChanged.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleSave = this.handleSave.bind(this);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getManagersPermissionResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (!res.errorMessage) {
                this.state.permission = res.object;
                this.setState({[this.state.permission]: this.state.permission});
                console.log(this.state.permission)
            }
        });
        stompClient.subscribe('/user/topic/setManagersPermissionResult', (r) => {
            const res = JSON.parse(r["body"]);
            this.state.hadError = res.errorMessage != null;
            this.state.resultMessage = this.state.hadError ? res.errorMessage : "Changed permissions successfully"
            if (!this.state.hadError) {
                handleClose();
            }
        });
        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getManagersPermissionResult')
        stompClient.unsubscribe('/user/topic/setManagersPermissionResult')
        this.mounted = false;
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    handleCheckboxChanged(value) {
        this.state.permission.permissionMask = this.state.permission.permissionMask ^ value;
        this.setState({[this.state.permission.permissionMask]: this.state.permission.permissionMask});
    }

    handleSave() {
        stompClient.send("/app/market/setManagersPermission", {}, JSON.stringify({
            "manager": this.props.manager,
            "storeId": this.props.storeId,
            "permissionMask": this.state.permission.permissionMask,
        }));
    }

    handleShow() {
        stompClient.send("/app/market/getManagersPermission", {}, JSON.stringify({
            "storeId": this.props.storeId,
            "manager": this.props.manager,
        }));
        setShow(true);
    }

    render() {
        return (
            <>
                    <Button variant="primary" onClick={this.handleShow} className={"editManagerButton"}>
                        Edit
                    </Button>
                    <Modal show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>Edit permissions</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {
                                <div>
                                    <div>
                                        <label><Checkbox checked={(this.state.permission.permissionMask & 0x1) != 0} onChange={(event) => {this.handleCheckboxChanged(0x1)}}/>Allow to assign managers</label>
                                    </div>
                                    <div>
                                        <label><Checkbox checked={(this.state.permission.permissionMask & 0x2) != 0} onChange={(event) => {this.handleCheckboxChanged(0x2)}}/>Allow to manage items</label>
                                    </div>
                                    <div>
                                        <label><Checkbox checked={(this.state.permission.permissionMask & 0x4) != 0} onChange={(event) => {this.handleCheckboxChanged(0x4)}}/>Allow to manage discounts</label>
                                    </div>
                                    <div>
                                        <label><Checkbox checked={(this.state.permission.permissionMask & 0x8) != 0} onChange={(event) => {this.handleCheckboxChanged(0x8)}}/> Allow to manage purchase policies</label>
                                    </div>
                                    <div>
                                        <label><Checkbox checked={(this.state.permission.permissionMask & 0x10) != 0} onChange={(event) => {this.handleCheckboxChanged(0x10)}}/> Allow to manage bids</label>
                                    </div>
                                </div>
                            }
                        </Modal.Body>
                        <Modal.Footer>
                            <ResultLabel text={this.state.resultMessage} hadError={this.state.hadError}/>
                            <Button variant="primary" onClick={this.handleSave}>
                                Save
                            </Button>
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

    let manager = props.manager
    let storeId = props.storeId
    return <>
        <ManagerPermissionPopup manager={manager} storeId={storeId}/>
    </>
}

export default wrapRender;