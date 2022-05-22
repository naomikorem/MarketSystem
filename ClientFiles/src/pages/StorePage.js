import React, {Component, useState} from "react";
import {useLocation, useParams} from "react-router-dom";
import {stompClient, connectedPromise} from "../App";
import Modal from "../Components/Modal";
import ResultLabel from "../Components/ResultLabel";
import TextField from "@material-ui/core/TextField";
import Select from "@material-ui/core/Select";
import {categories} from "../Shared/Shared";

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
                        Category: {item.category}<br/>
                        Rating: {parseFloat(item.rate).toFixed(2)}</p>
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
            is_error: true,

            itemName: "",
            itemCategory: "",
            keyWords: [],
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeKeywords = this.handleChangeKeywords.bind(this);
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value //set this.state.value to the input's value
        });
    }

    handleChangeKeywords(event) {
        if (event.target.value === "") {
            this.state.keyWords = []
        } else {
            this.state.keyWords = event.target.value.split(/[, ]+/);
        }
        this.setState({
            [event.target.name]: this.state.keyWords //set this.state.value to the input's value
        });
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
            }
            else {
                this.state.is_error = false
                this.setState({[this.state.is_error]: false});
                this.state.error = `Successfully added ${res_item.object.amount} items to cart`;


                const index = this.state.listitems.findIndex(item => item.item_id === res_item.object.item_id)
                this.state.listitems[index].amount = previous_amount - res_item.object.amount
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

            <div className={"searchBarMargin"}><TextField id="outlined-basic" label="Item name" variant="filled" className={"searchBar"} value={this.state.itemName} onChange={this.handleChange} name="itemName"/></div>
            <div className={"searchBarMargin"}><Select className={"searchBar"} style={{background: "#FFFFFF"}} name="itemCategory" defaultValue={"Food"} value={this.state.itemCategory} labelId="categoryLabel" id="category" label="Category" variant="filled" onChange={this.handleChange}>
                <option value={""}>All items</option>
                { categories.map((s, i) => <option key={i} value={s}>{s}</option>)}
            </Select></div>
            <div className={"searchBarMargin"}><TextField id="outlined-basic" label="Item keywords" variant="filled" className={"searchBar"} value={this.state.keyWords} onChange={this.handleChangeKeywords} name="keyWords"/></div>
            <div className="store-grid-container">

                {this.state.listitems.filter(s => {
                    return s.product_name.includes(this.state.itemName) && s.category.includes(this.state.itemCategory) &&
                        (this.state.keyWords.length === 0 || this.state.keyWords.some(k => s.keyWords.some(itemKey => itemKey.includes(k))))
                }).map((listitem) => (

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
    const location = useLocation();
    const { storeName } = location.state;

    return <div>
        <StorePage storeid={storeid}
                   storeName={storeName}/>
    </div>
}

export default wrapRender;