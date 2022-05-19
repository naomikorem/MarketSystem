import React, {Component, useState} from "react";
import {useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from "../Components/Modal";

function StoreItem(props) {
    const item = props.item;
    let [modalOpen, setModalOpen] = useState(false);

    return (

        <div>
            <article onClick={() => {setModalOpen(true)}} key={item.id} className={"items-grid"}>
                <div>
                    <h1>{item.product_name}</h1>
                    <p>Amount Left: {item.amount}<br/>
                        Category: {item.category}</p>
                    <h2>₪{item.price}</h2>
                </div>
            </article>

        {modalOpen && <Modal amount={item.amount}
                             product_name = {item.product_name}
                             setOpenModal={setModalOpen} />}
        </div>
    );
}


class StorePage extends Component {

    constructor(props) {
        super(props);
        console.log(props.storeid)

        this.state = {
            listitems: []
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

            }
        });
        stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeid}));
    }

    render() {
        return (
        <React.Fragment>
          <div className="formCenter">
            <h1>Welcome to store: {this.props.storeName}</h1>
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
}

function wrapRender() {
    let {storeid, name} = useParams();
    return <div>
        <StorePage storeid={storeid}
                   storeName={name}/>
    </div>
}

export default wrapRender;