import React, { Component, createContext, useState} from "react";
import {
  BrowserRouter,
  Routes,
  Route, NavLink, Link, useNavigate
} from "react-router-dom";

import SignUpForm from "./pages/SignUpForm";
import SignInForm from "./pages/SignInForm";
import HomePage from "./pages/HomePage";
import StorePage from "./pages/StorePage";
import ManageStores from "./pages/ManageStores";
import EditStorePage from "./pages/EditStorePage";
import CloseStorePermanentlyPage from "./pages/CloseStorePermanently"
import LogoutButton from "./Components/LogoutButton";
import LoginButton from "./Components/LoginButton";
import RegisterButton from "./Components/RegisterButton";
import RemoveUserSubscription from "./pages/RemoveUserSubscriptionPage"
import UserProfile from "./pages/UserProfilePage"
import PersonalPurchaseHistory from "./pages/PersonalPurchaseHistory"
import UserPurchaseHistory from "./pages/ViewUserPurchaseHistoryAdminPage"
import StoreHistoryPage from "./pages/StoreHistoryPage"
import GetSubscriberInfoPage from "./pages/GetSubscriberInfoAdminPage"
import AddService from "./pages/AddExternalService"
import PersonalPurchaseHistoryButton from "./Components/PersonalPurchaseHistoryButton"
import UserHistoryButton from "./Components/UserHistoryButton"
import StoreHistoryButton from "./Components/StoreHistoryButton"
import CloseStorePermanentlyButton from "./Components/CloseStorePermanentlyButton"
import UnsubscribeUserButton from "./Components/UnsubscribeUserButton";

import "./App.css";

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import OpenNewStore from "./pages/OpenNewStore";
import ErrorPage from "./pages/ErrorPage";
import Cart from "./Components/cart";
import Modal from "./Components/Modal";
import StorePurchaseHistory from "./pages/ViewStorePurchaseHistoryAdminPage";
import Navbar from "./Components/Navbar";
import {IoNotificationsSharp, AiOutlineShoppingCart} from "react-icons/all";
import Button from "react-bootstrap/Button";
import NotificationPage from "./pages/NotificationPage";
import BackButton from "./Components/BackButton";
import AddExternalService from "./pages/AddExternalService";

import NotificationBadge from "react-notification-badge";
import {Effect} from 'react-notification-badge';
import AddAdmin from "./pages/AddAdmin";


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
export let [isAdmin, setIsAdmin] = [undefined, undefined]
export let [notifications, setNotifications] = [undefined, undefined]
export let [newNotificationsCounter, setNewNotificationsCounter] = [undefined, undefined]


async function loginByToken() {
  await connectedPromise;
  stompClient.unsubscribe('/user/topic/loginByTokenResult')
  stompClient.subscribe('/user/topic/loginByTokenResult', (r) => {
    let res = JSON.parse(r["body"]);
    if (!res.errorMessage) {
      sessionStorage.setItem('user', JSON.stringify(res.object))
      setUser(res.object)
      stompClient.send("/app/market/isAdmin", {}, JSON.stringify());
    }
    if (user === "null") {
      setUser(null)
    }
  });
  stompClient.send("/app/market/loginByToken", {}, JSON.stringify({"token" : token}));
}

function render() {
  [user, setUser] = useState(sessionStorage.getItem('user'));
  [token, setToken] = useState(sessionStorage.getItem('token'));
  [isAdmin, setIsAdmin] = useState(false);
  [notifications, setNotifications] = useState([]);
  [newNotificationsCounter, setNewNotificationsCounter] = useState(0);

  if (token != null && token !== '') {
    loginByToken();
  }

  return (
      <BrowserRouter >
        <div className="App">
          { user != null ?
            <Navbar/> : null
          }

          <div className="appAside" />
          <div className="appForm">
            <React.Fragment>
              <BackButton/>
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
              <Route exact path="/" element={<HomePage/>} />
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
              <Route path="/view-subscriber-info-admin" element={<GetSubscriberInfoPage/>} />
              <Route path="/personal-purchase-history" element={<PersonalPurchaseHistory/>}/>
              <Route path="/user-purchase-history" element={<UserPurchaseHistory/>} />
              <Route path="/select-store-history" element={<StorePurchaseHistory/>} />
              <Route path="/add-external-service" element={<AddService/>} />
              <Route path="/store-purchase-history/:storeid" element={<StoreHistoryPage/>} />
              <Route path="/my-notifications" element={<NotificationPage/>} />
              <Route path="/popup" element={<Modal/>} />
              <Route path="/edit-store/:storeid" element={<EditStorePage/>} />
              <Route path="/your-cart" element={<Cart/>} />
              <Route path="/add-admin" element={<AddAdmin/>} />
              <Route path="/*" element={<ErrorPage/>} />
            </Routes>
          </div>

              <div className="notification-icon">
                { user != null ?
                    <div>
                      <Link to="my-notifications">
                        <NotificationBadge count={newNotificationsCounter} effect={Effect.SCALE}/>
                        <Button className="transparent-button">
                          <IoNotificationsSharp/>
                        </Button>
                      </Link>
                    </div>
                : null}
            <Link to="your-cart">
              <Button className="transparent-button">
                <AiOutlineShoppingCart />
              </Button>
            </Link>
          </div>
        </div>

      </BrowserRouter>
  );
}


export default render;