import React, {Component} from "react";
import {user} from "../App";

class UserProfile extends Component {

    render() {
        return (
            user != null ?
            <div className="formCenter">
                <h1>Hello {user.userName} to your profile page!</h1>
                <div className="formField">
                    <label className="formFieldLabel">
                        First Name: {user.firstName}
                    </label>
                    <label className="formFieldLabel">
                        Last Name: {user.lastName}
                    </label>
                    <label className="formFieldLabel">
                        Email: {user.email}
                    </label>
                </div>
            </div> : null
        );
    }
}

export default UserProfile;