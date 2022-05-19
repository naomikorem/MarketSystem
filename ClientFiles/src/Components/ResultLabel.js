import React, {Component} from "react";


class ResultLabel extends Component {


    render() {
        return (
            <div>
        <label className={this.props.hadError ? "errorLabel" : "successLabel"}>
            {this.props.text}
        </label>
            </div>)
    }
}

export default ResultLabel;