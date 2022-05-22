import React from "react";
import "./Modal.css";

function AreYouSureModal(props) {
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
                    <h1>{props.title}</h1>
                </div>
                <div className="body">
                    <p>{props.body}</p>

                </div>
                <div className="footer">
                    <button
                        onClick={() => {
                            props.setOpenModal(false);
                        }}
                        id="cancelBtn"
                    >
                        {props.regretActionButton}
                    </button>
                    <button onClick={() => {
                            props.setOpenModal(false);
                            props.onContinue()}}>
                        {props.doActionButton}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default AreYouSureModal;