import React, { Component } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import { useNavigate } from 'react-router';

import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import MainPage from "./pages/MainPage";
import HomePage from "./pages/HomePage";
import StorePage from "./pages/StorePage";

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import OpenNewStore from "./pages/OpenNewStore";
import ErrorPage from "./pages/ErrorPage";
import Cart from "./components/cart";

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
              <Route path="/open-new-store" element={<OpenNewStore/>} />
              <Route path="/store/:storeid" element={<StorePage/>} />
              <Route path="/modify-cart" element={<Cart/>} />
              <Route path="/*" element={<ErrorPage/>} />
            </Routes>
          </div>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
