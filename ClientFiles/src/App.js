import logo from './logo.svg';
import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import './App.css';
import Button from 'react-bootstrap/Button';
import {CloseButton} from "react-bootstrap";


var socket = new SockJS("http://localhost:8080/market");
var stompClient = Stomp.over(socket);
stompClient.connect({}, function( frame ){
    console.log( "Connected :- "+frame );
    stompClient.subscribe('/user/topic/loginResult', function( notifications ) {
        console.log("printing result" + notifications);
    });
}, function( error ) {
    alert( error );
});

function activateLasers() {
    stompClient.send('/app/market/login', {}, JSON.stringify({'user': "user", 'pass': "pass"}), 0)
}
function App() {

  return (

          <button onClick={activateLasers}>
              asd
          </button>
  );
}

export default App;
