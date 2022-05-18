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
import LogoutButton from "./Components/LogoutButton";
import LoginButton from "./Components/LoginButton";
import RegisterButton from "./Components/RegisterButton";

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

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
                <LoginButton/>
                <RegisterButton/>
                <LogoutButton/>
              </div>

            </React.Fragment>

            <Routes>
            <Route exact path="/" element={<MainPage/>} />
            <Route path="/sign-in" element={<SignInForm/>} />
            <Route path="/sign-up" element={<SignUpForm/>} />
            <Route path="/home" element={<HomePage/>} />
            <Route path="/store/:storeid" element={<StorePage/>} />
            </Routes>



          </div>

        </div>
      </BrowserRouter>
    );
  }


export default render;
