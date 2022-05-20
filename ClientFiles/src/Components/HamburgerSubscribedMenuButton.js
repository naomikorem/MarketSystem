import React from "react";
import PropTypes from "prop-types";
import {NavDropdown} from "react-bootstrap";
import {user} from "../App";

export default function HamburgerMenu(props) {
    if (user != null) {
        return (
            <div>
                <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                    <NavDropdown.Item href="/user-profile-subscriber">View Profile</NavDropdown.Item>
                    <NavDropdown.Item href="/personal-purchase-history">View Personal Purchase
                        history</NavDropdown.Item>
                    <NavDropdown.Item href="/manage-stores">Manage My Stores</NavDropdown.Item>
                    <NavDropdown.Divider/>
                </NavDropdown>
            </div>
        );
    } else {
        return null
    }
}
