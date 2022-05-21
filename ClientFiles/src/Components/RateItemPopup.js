import React from "react";
import "./Modal.css";

function RateItemPopup(props) {
    return (
        <div className="modalBackground">
            <div className="modalContainer">
                <div className="titleCloseBtn">
                    <button
                        onClick={() => {
                            props.setOpenPopup(false);
                        }}
                    >
                        X
                    </button>
                </div>
                <div className="title">
                    <h1>Rate the item: {props.product_name}</h1>
                </div>
                <div className="body">
                    <p>The rate should be <br/>
                        a number between 0-5</p>

                </div>
                <div className="input-container">
                    <input className="input-text" type="number" id="input-rate" name="rate"/>
                </div>
                <div className="footer">
                    <button
                        onClick={() => {
                            props.setOpenPopup(false);
                        }}
                        id="cancelBtn"
                    >
                        Cancel
                    </button>
                    <button onClick={() => {
                        props.setOpenPopup(false);
                        props.onContinue(props.id, document.getElementById("input-rate").value);}}>
                        Continue
                    </button>
                </div>
            </div>
        </div>
    );
}

export default RateItemPopup;