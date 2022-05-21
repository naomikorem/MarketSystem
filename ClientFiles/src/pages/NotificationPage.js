import React, { Component } from "react";

class NotificationPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            listitems: ["notification1", "notification2","notification3","notification4","notification5","notification6","notification7"],
            error: "",
        };
        this.handleChange = this.handleChange.bind(this);
    }


    // async componentDidMount() {
    //     await connectedPromise;
    //     stompClient.subscribe('/user/topic/notificationResult', (r) => {
    //         const res = JSON.parse(r["body"]);
    //         if (res.errorMessage == null)
    //         {
    //             this.state.listitems = res.object;
    //             this.setState({[this.state.listitems]: this.state.listitems});
    //         }
    //         else
    //         {
    //             this.state.error = res.errorMessage;
    //             this.setState({[this.state.error]: this.state.error});
    //         }
    //     });
    //     stompClient.send("/app/market/get", {}, {});
    // }
    //
    // componentWillUnmount() {
    //     stompClient.unsubscribe('/user/topic/getOpenStoresResult');
    // }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    render() {
        return (
            <div>
                <h1 align="center">Your Notifications</h1>

                <div className="store-grid-container">
                    {this.state.listitems.map((listitem, index) => (
                        <div key={index} className={"store-grid-item"}>
                           <label>
                               {listitem}
                           </label>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

function wrapRender() {
    return <div>
        <NotificationPage/>
    </div>
}

export default wrapRender;
