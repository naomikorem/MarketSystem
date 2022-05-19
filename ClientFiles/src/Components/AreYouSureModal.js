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
                    <h1>Are You Sure?</h1>
                </div>
                <div className="body">
                    <p>This action is not a reversible</p>

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
                            props.onContinue(props.id)}}>
                        I'm Sure
                    </button>
                </div>
            </div>
        </div>
    );
}

export default AreYouSureModal;