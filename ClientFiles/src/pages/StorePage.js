import React, {Component, useState} from "react";
import {useLocation, useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from "../Components/Modal";
import ResultLabel from "../Components/ResultLabel";

let previous_amount = 0;

function StoreItem(props) {
    const item = props.item;
    let [modalOpen, setModalOpen] = useState(false);

    let onContinue = (item_id, amount) => {
        previous_amount = item.amount;
        stompClient.send("/app/market/AddItemToCart", {}, JSON.stringify({"store_id": props.storeid, "item_id" : item_id, "amount" : amount}));
    };

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

        {modalOpen && <Modal
                            id={item.item_id}
                            amount={item.amount}
                            product_name = {item.product_name}
                            setOpenModal={setModalOpen}
                            onContinue={onContinue}/>}
        </div>
    );
}


class StorePage extends Component {

    constructor(props) {
        super(props);
        console.log(this.props)

        this.state = {
            listitems: [],
            error: "",
            is_error: true
        };
    }

    async componentDidMount() {
        await connectedPromise;

        stompClient.subscribe('/user/topic/AddItemToCartResult', (r) => {
            const res_item = JSON.parse(r["body"]);
            const errorMsg = res_item.errorMessage;

            if (errorMsg) {
                this.state.error = errorMsg;
                this.state.is_error = true
                this.setState({[this.state.is_error]: true});
                console.log("is error")
                console.log(this.state.is_error)
            }
            else {
                this.state.is_error = false
                this.setState({[this.state.is_error]: false});
                this.state.error = `Successfully added ${res_item.object.amount} items to cart`;


                const index = this.state.listitems.findIndex(item => item.item_id === res_item.object.item_id)
                this.state.listitems[index].amount = previous_amount - res_item.object.amount
                console.log(this.state.listitems[index])
                this.setState({[this.state.listitems]: this.state.listitems});
            }
        });

        stompClient.subscribe('/user/topic/getStoreItemsResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.listitems = response.object
                this.setState({[this.state.listitems]: this.state.listitems});
            }
        });
        stompClient.send("/app/market/getStoreItems", {}, JSON.stringify({"id" : this.props.storeid}));
    }

    render() {
        return (
        <React.Fragment>
          <div className="formCenter">
            <h1 align="center">Welcome to {this.props.storeName}</h1>
          </div>
            <div className="store-grid-container">
                {this.state.listitems.map((listitem) => (

                    <StoreItem
                        key={listitem.id}
                        item = {listitem}
                        storeid = {this.props.storeid}
                    />
                ))}
            </div>
            <div>
                <ResultLabel text={this.state.error} hadError={this.state.is_error}/>
            </div>
        </React.Fragment>
    );
  }
}

function wrapRender() {
    let {storeid} = useParams();
    const location = useLocation()
    const { storeName } = location.state

    return <div>
        <StorePage storeid={storeid}
                   storeName={storeName}/>
    </div>
}

export default wrapRender;