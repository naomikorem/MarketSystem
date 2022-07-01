import React, {useState} from "react";
import "./Modal.css";
import {isAdmin, setIsAdmin} from "../App";

function Modal(props) {
    let [isBid, setIsBid] = useState(false);
    return (
        <div className="modalBackground">
            <div className="modalContainer">
                <div className="titleCloseBtn">
                    <button
                        onClick={() => {
                            props.setOpenModal(false);
                        }}
                    >
                        X
                    </button>
                </div>
                <div className="title">
                    <h1>{props.product_name}</h1>
                </div>
                <div className="body">
                    <p>Choose amount out of {props.amount}</p>

                </div>
                <div className="input-container">
                    <input className="input-text" type="number" id="input-amount" name="amount" min="1" max={props.amount}/>
                    <div>
                        <button onClick={() =>{setIsBid(!isBid);}}>Bid</button>
                        {isBid && <input className="input-text" type="number" id="input-bid" name="bid" min="0"/>}
                    </div>
                </div>
                <div className="footer">
                    <button
                        onClick={() => {
                            props.setOpenModal(false);
                        }}
                        id="cancelBtn"
                    >
                        Cancel
                    </button>
                    <button onClick={() => {
                            props.setOpenModal(false);
                            isBid ? props.onContinue(props.id, document.getElementById("input-amount").value, isBid, document.getElementById("input-bid").value):
                                props.onContinue(props.id, document.getElementById("input-amount").value, isBid,0);}}>
                        Continue
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Modal;