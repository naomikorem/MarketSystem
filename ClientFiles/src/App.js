import React, { Component } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import MainPage from "./pages/MainPage";
import HomePage from "./pages/HomePage";

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


class App extends Component {
  render() {
    return (
      <BrowserRouter >
        <div className="App">
          <div className="appAside" />
          <div className="appForm">
            <Routes>
            <Route exact path="/" element={<SignInForm/>} />
            <Route path="/sign-in" element={<SignInForm/>} />
            <Route path="/sign-up" element={<SignUpForm/>} />
            <Route path="/home" element={<HomePage/>} />
            </Routes>
          </div>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
