import React from "react";
import "./Modal.css";

function ModalBid(props) {
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
                        props.onContinue(props.id, document.getElementById("input-amount").value);}}>
                        Continue
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ModalBid;