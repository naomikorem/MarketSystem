import {stompClient, connectedPromise} from "../App";
import React, { Component } from "react";
import {Link} from "react-router-dom";
//import { useNavigate } from "react-router-dom";
//import {useNavigate} from "react-router-dom";
//
// // v6 examples

// Navigate to new URL
//navigate("/keyhole")

class HomePage extends Component {
    constructor() {
        super();
        stompClient.send("/app/market/getStores", {}, {});
    }

    state = {
        listitems: [],
        error: ""
    };

    handleClick = (index) => {
        console.log("click" + index);
        console.log(this.state.listitems[index]);
        // const navigate = useNavigate();
        // navigate('sign-in');
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (res.errorMessage == null)
            {
                this.state.listitems = res.object.map(store => store.name);
                this.setState({[this.state.listitems]: this.state.listitems});
            }
            else
            {
                this.state.error = res.errorMessage;
                this.setState({[this.state.error]: this.state.error});
            }
        });
    }

    render() {
        return (
            <React.Fragment>
                <h1>Choose store</h1>
                <ul className="list-group">
                    {this.state.listitems.map((listitem, index) => (
                        // <li onClick={() => this.handleClick(index)}>
                        <li>
                            <Link to={"/sign-in"}>
                                {listitem}
                            </Link>
                        </li>
                    ))}
                </ul>
                <label className="errorLabel">
                    {this.state.error}
                </label>
            </React.Fragment>
        );
    }
}

export default HomePage;


// import React, { useState } from "react";
// import { v4 as uuidv4 } from "uuid";
//
// const initialList = [
//     {
//         id: "a",
//         name: "Robin"
//     },
//     {
//         id: "b",
//         name: "Dennis"
//     }
// ];
//
// const HomePage = () => {
//     const [list, setList] = useState(initialList);
//     const [name, setname] = useState("");
//
//     const handleChange = (event) => {
//         setname(event.target.value);
//     };
//
//     const handleKeyDown = (event) => {
//         if (event.key === "Enter") {
//             handleAdd();
//         }
//     };
//
//     const handleAdd = () => {
//         const newList = list.concat({ name, id: uuidv4() });
//         setList(newList);
//         setname("");
//     };
//
//     return (
//         <div>
//             <AddItem
//                 name={name}
//                 onChange={handleChange}
//                 onAdd={handleAdd}
//                 handleKeyDown={handleKeyDown}
//             />
//             <List list={list} />
//         </div>
//     );
// };
//
// const AddItem = ({ onChange, name, onAdd, handleKeyDown }) => {
//     return (
//         <div>
//             <div>
//                 <input
//                     type="text"
//                     value={name}
//                     onChange={onChange}
//                     onKeyDown={handleKeyDown}
//                 />
//                 <button type="button" onClick={onAdd}>
//                     Add
//                 </button>
//             </div>
//         </div>
//     );
// };
//
// const List = ({ list }) => {
//     return (
//         <form>
//             {list.map((item) => {
//                 return <li key={item.id}>{item.name}</li>;
//             })}
//         </form>
//     );
// };
//
// export default HomePage;
