import React, {Component} from "react";

const ItemPopup = props =>
{
    return (
        <div className="popup-box">
            <div className="box">
                <button className="close-btn" onClick={props.handleClose}> x </button>
                This is my popup
            </div>
        </div>
    )
}

export default ItemPopup;