import {useNavigate} from "react-router-dom";
import React, {Component} from "react";
import {RiArrowGoBackFill} from "react-icons/ri";
import Button from "react-bootstrap/Button";

class BackButton extends Component{
    constructor(props) {
        super(props);
    }

    render() {
        return (
        <Button className="transparent-button" onClick={() => this.props.navigate(-1)}><RiArrowGoBackFill/></Button>)
    }

}

function wrapRender() {
    let navigate = useNavigate();

    return <div>
        <BackButton navigate={navigate}/>
    </div>
}

export default wrapRender;