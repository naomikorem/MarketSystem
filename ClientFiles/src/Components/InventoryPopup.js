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

let [show, setShow] = [undefined, undefined];
let handleClose = () => undefined;


class InventoryPopup extends Component {

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
        stompClient.subscribe('/user/topic/getStoreItemsResult', (r) => {
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
        stompClient.unsubscribe('/user/topic/getStoreItemsResult')
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
        stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeId}));
        setShow(true);
    }

    render() {
        return (
            <>
                <Button variant="primary" onClick={this.handleShow} className={"storeEditButton"}>
                    Manage inventory
                </Button>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Manage inventory</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.items.map((listitem, index) => (

                            <EditItemPopup  key={index} storeId={this.props.storeId} itemDTO={listitem}/>
                        ))}
                        <AddItemPopup storeId={this.props.storeId}/>
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
        <InventoryPopup storeId={storeId}/>
    </>
}

export default wrapRender;