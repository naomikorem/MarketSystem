import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Popup from 'react-popup';
import Modal from "../Components/Modal";

class StoreItem extends Component {
    addToCart() {
        Popup.alert("asdsdfdf");
        console.log("dsfdgfhg")
    }

    render() {
        const item = this.props.item;

        //const [modalOpen, setModalOpen] = useState(false);

        return (

            <div>
                <button onClick={() => {setModalOpen(true);}} key={item.id} className={"items-grid"}>
                    <div>
                        <h1>{item.product_name}</h1>
                        <p>Amount Left: {item.amount}<br/>
                            Category: {item.category}</p>
                        <h2>₪{item.price}</h2>
                    </div>
                </button>

            {/*{modalOpen && <Modal setOpenModal={setModalOpen} />}*/}
            </div>
        );
    }
}


class StorePage extends Component {

    constructor() {
        super();

        this.state = {
            listitems: [],
            storeName: ""
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreItemsResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.listitems = response.object
                this.setState({[this.state.listitems]: this.state.listitems});
                console.log(this.state.listitems);
                //console.log(this.state.listitems[0].product_name)
                //console.log(this.state.listitems.map((listitem) => (listitem.)));

            }
        });
        stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeid}));
    }

    render() {
        return (
        <React.Fragment>
          <div className="formCenter">
            <h1>Welcome to store {this.state.storeName}</h1>
          </div>
            <div className="store-grid-container">
                {this.state.listitems.map((listitem) => (

                    <StoreItem
                        key={listitem.id}
                        item = {listitem}
                    />
                ))}
            </div>
        </React.Fragment>
    );
  }

    // renderItem(listitem) {
    //     return(
    //         <div key={listitem.id} className={"store-grid-item"}>
    //             {/*<Link to={`/store/${listitem.id}`} className="storeLink">*/}
    //             {/*    {listitem.name}*/}
    //             {/*</Link>*/}
    //             <label>
    //                 {listitem.product_name}
    //             </label>
    //         </div>);
}

function wrapRender() {
    let {storeid } = useParams();
    return <div>
        <StorePage storeid={storeid}/>
    </div>
}

export default wrapRender;