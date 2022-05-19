import React, { Component, createContext, useState} from "react";
import {
  BrowserRouter,
  Routes,
  Route,  NavLink
} from "react-router-dom";
import { useNavigate, } from 'react-router';

import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import MainPage from "./pages/MainPage";
import HomePage from "./pages/HomePage";
import StorePage from "./pages/StorePage";
import ManageStores from "./pages/ManageStores";
import LogoutButton from "./Components/LogoutButton";
import LoginButton from "./Components/LoginButton";
import RegisterButton from "./Components/RegisterButton";
import ManageStoresButton from "./Components/ManageStoresButton";

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import OpenNewStore from "./pages/OpenNewStore";
import ErrorPage from "./pages/ErrorPage";
import Cart from "./Components/cart";
import Modal from "./Components/Modal";

const socket = new SockJS("http://localhost:8080/market");
export const stompClient = Stomp.over(socket);
export const connectedPromise = new Promise(resolve => {
  stompClient.connect({}, function (frame) {
    resolve(true)
  }, function (error) {
    alert(error);
  });
})

export let [user, setUser] = [undefined, undefined]






function  render() {
    [user, setUser] = useState(sessionStorage.getItem('user'))

    //const [modalOpen, setModalOpen] = useState(false);

    return (
      <BrowserRouter >
        <div className="App">
          <div className="appAside" />
          <div className="appForm">

            <React.Fragment>
              <div className="pageSwitcher">
                <NavLink
                    to="/home"
                    className={(navData) => navData.isActive ? "pageSwitcherItem-active" : "pageSwitcherItem"}
                >
                  Home
                </NavLink>
                <ManageStoresButton/>
                <LoginButton/>
                <RegisterButton/>
                <LogoutButton/>
              </div>

            </React.Fragment>

            <Routes>
              <Route exact path="/" element={<SignInForm/>} />
              <Route path="/sign-in" element={<SignInForm/>} />
              <Route path="/sign-up" element={<SignUpForm/>} />
              <Route path="/home" element={<HomePage/>} />
              <Route path="/open-new-store" element={<OpenNewStore/>} />
              <Route path="/store/:storeid" element={<StorePage/>} />
              <Route path="/modify-cart" element={<Cart/>} />
              <Route path="/manage-stores" element={<ManageStores/>} />
              <Route path="/popup" element={<Modal/>} />
              <Route path="/*" element={<ErrorPage/>} />
            </Routes>



          </div>

        </div>
      </BrowserRouter>
    );
}


export default render;
