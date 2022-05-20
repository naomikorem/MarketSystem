import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import ResultLabel from "../Components/ResultLabel";
import AreYouSureModal from "../Components/AreYouSureModal";


function RemoveAlert(props) {
    let [modalOpen, setModalOpen] = useState(false);

    const handleApply = () => {
        stompClient.send("/app/market/removeSubscription", {}, JSON.stringify({"username" : props.username_to_unsubscribe}));
    }

    return (
        <div>
            <div className="formField">
                <button onClick={() => {setModalOpen(true)}} className="formFieldButton">Remove</button>
            </div>

            {modalOpen && <AreYouSureModal
                title="Are You Sure?"
                body="This action is not a reversible"
                doActionButton="I'm Sure"
                regretActionButton="Cancel"
                setOpenModal={setModalOpen}
                onContinue={handleApply}/>}
        </div>
    );
}


class RemoveUserSubscription extends Component {

    constructor() {
        super();

        this.label = "errorLabel";

        this.state = {
            username_to_unsubscribe: "",
            message: "",
            hadError: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        let target = event.target;
        let value = target.value;
        let name = target.name;

        this.setState({
            [name]: value
        });
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/removeSubscriptionResult', (r) => {
            let response = JSON.parse(r["body"]);
            console.log(response)
            if(response.errorMessage)
            {
                console.log(response.errorMessage);
                this.state.message = response.errorMessage
                this.state.hadError = true;
                this.setState({[this.state.hadError]: this.state.hadError});
            }
            else if (response.object)
            {
                this.state.message = "Removed the user's subscription";
            }
            else
            {
                console.log(response.errorMessage);
                this.state.message = "Could not remove the user's subscription";
                this.state.hadError = true;
                this.setState({[this.state.hadError]: this.state.hadError});
            }
            this.setState({[this.state.message]: this.state.message});
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/removeSubscriptionResult');
    }

    render() {
        return (
            <div className="formCenter">
                <h1>Please enter name of subscribed user</h1>
                <div className="formField">
                    <label className="formFieldLabel">
                        Username to remove his subscription
                    </label>
                    <input
                        type="username_to_unsubscribe"
                        id="username_to_unsubscribe"
                        className="formFieldInput"
                        placeholder="Enter name of subscribed user"
                        name="username_to_unsubscribe"
                        value={this.state.username_to_unsubscribe}
                        onChange={this.handleChange}
                    />
                </div>

                <RemoveAlert username_to_unsubscribe = {this.state.username_to_unsubscribe}/>

                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </div>
        );
    }
}

export default RemoveUserSubscription;