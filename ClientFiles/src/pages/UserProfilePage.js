import React, {Component} from "react";
import {user} from "../App";
import {Link} from "react-router-dom";

class UserProfile extends Component {

    render() {
        return (
            user != null ?
                <div className="formCenter">
                    <div className="formField">
                        <label className="ProfileTitle">
                            Hello @{user.userName} to your profile page!
                        </label>
                    </div>
                    <div className="formField">
                        <label className="formFieldProfileLabel">
                            First Name:
                        </label>
                        <label className="formFieldProfileInputLabel">
                            {user.firstName}
                        </label>
                    </div>
                    <div className="formField">
                        <label className="formFieldProfileLabel">
                            Last Name:
                        </label>
                        <label className="formFieldProfileInputLabel">
                            {user.lastName}
                        </label>
                    </div>
                    <div className="formField">
                        <label className="formFieldProfileLabel">
                            Email:
                        </label>
                        <label className="formFieldProfileInputLabel">
                            {user.email}
                        </label>
                    </div>
                    <div className="formField">
                        <Link to="/your-cart">
                            <button className="formFieldShoppingCartButton">View Your Shopping Cart</button>
                        </Link>
                    </div>
            </div> : null
        );
    }
}

export default UserProfile;