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
import EditStorePage from "./pages/EditStorePage";
import CloseStorePermanentlyPage from "./pages/CloseStorePermanently"
import LogoutButton from "./Components/LogoutButton";
import LoginButton from "./Components/LoginButton";
import RegisterButton from "./Components/RegisterButton";
import ProfileButton from "./Components/ProfileButton"
import ManageStoresButton from "./Components/ManageStoresButton";
import RemoveUserSubscription from "./pages/RemoveUserSubscriptionPage"
import UserProfile from "./pages/UserProfilePage"
import PersonalPurchaseHistory from "./pages/PersonalPurchaseHistory"
import UserPurchaseHistory from "./pages/ViewUserPurchaseHistoryAdminPage"
import StoreHistoryPage from "./pages/StoreHistoryPage"
import PersonalPurchaseHistoryButton from "./Components/PersonalPurchaseHistoryButton"
import UserHistoryButton from "./Components/UserHistoryButton"
import StoreHistoryButton from "./Components/StoreHistoryButton"
import CloseStorePermanentlyButton from "./Components/CloseStorePermanentlyButton"
import UnsubscribeUserButton from "./Components/UnsubscribeUserButton";
import HamburgerMenu from "./Components/HamburgerSubscribedMenuButton"

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import OpenNewStore from "./pages/OpenNewStore";
import ErrorPage from "./pages/ErrorPage";
import Cart from "./Components/cart";
import Modal from "./Components/Modal";
import StorePurchaseHistory from "./pages/ViewStorePurchaseHistoryAdminPage";


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
export let [token, setToken] = [undefined, undefined]




async function loginByToken() {
  await connectedPromise;
  stompClient.subscribe('/user/topic/loginByTokenResult', (r) => {
    let res = JSON.parse(r["body"]);
    if (!res.errorMessage) {
      sessionStorage.setItem('user', JSON.stringify(res.object))
      setUser(res.object)
    }
  });
  stompClient.send("/app/market/loginByToken", {}, JSON.stringify({"token" : token}));
}

function render() {
    [user, setUser] = useState(sessionStorage.getItem('user'));
    [token, setToken] = useState(sessionStorage.getItem('token'));

    if (token != null && token !== '') {
        loginByToken();
    }

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
                <StoreHistoryButton/>
                <UserHistoryButton/>
                <PersonalPurchaseHistoryButton/>
                <UnsubscribeUserButton/>
                <CloseStorePermanentlyButton/>
                <ProfileButton/>
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
              <Route path="/close-store-permanently-admin" element={<CloseStorePermanentlyPage/>} />
              <Route path="/manage-stores" element={<ManageStores/>} />
              <Route path="/remove-user-subscription-admin" element={<RemoveUserSubscription/>}/>
              <Route path="/user-profile-subscriber" element={<UserProfile/>} />
              <Route path="/personal-purchase-history" element={<PersonalPurchaseHistory/>}/>
              <Route path="/user-purchase-history" element={<UserPurchaseHistory/>} />
              <Route path="/select_store-history" element={<StorePurchaseHistory/>} />
              <Route path="/store-purchase-history/:storeid" element={<StoreHistoryPage/>} />
              <Route path="/popup" element={<Modal/>} />
              <Route path="/edit-store/:storeid" element={<EditStorePage/>} />
              <Route path="/*" element={<ErrorPage/>} />
            </Routes>
          </div>

        </div>
      </BrowserRouter>
    );
}


export default render;
