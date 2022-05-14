import logo from './logo.svg';
import Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import './App.css';

var socket = new SockJS("http://localhost:8080/market");
var stompClient = Stomp.over(socket);
stompClient.connect();

function activateLasers() {
    stompClient.send('/app/market/enter/aaa', {}, {}, 0);
    stompClient.send('/app/market/login', {}, JSON.stringify({'user': "user", 'pass': "pass"}), 0)
    console.log("here");
}
function App() {

  return (

    <div className="App">
      <button onClick={activateLasers}>
        Activate Lasers
      </button>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          //href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
