import logo from './logo.svg';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {Route, Router, Routes } from "react-router-dom";
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap";
import SignInForm from "./Pages/SignInForm";

//import SignUpForm from './Pages/SignUpForm';
//import SignInForm from './Pages/SignInForm';

var socket = new SockJS("http://localhost:8080/market");
export var stompClient = Stomp.over(socket);
stompClient.connect({}, function( frame ){
    stompClient.subscribe('/user/topic/loginResult', function( notifications ) {
    });
}, function( error ) {
    alert( error );
});


function App() {
  return SignInForm();
}

export default App;
