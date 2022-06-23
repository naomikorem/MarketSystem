import React, {Component} from "react";
import BasketItem from './basketItem'
import "../App.css" ;
import {AiOutlineDelete} from "react-icons/all";
import {stompClient} from "../App";
import EditBidPopup from "./EditBidPopup";
import EditBidCostumerPopup from "./EditBidCostumerPopup";

class Bids extends Component{
    renderItems = () => {
        if(this.props.items == null || this.props.items.length === 0) return <p>You don't have any bids</p>;
        return (
            !this.props.inCart ?
            <table className={"basket-grid"} >
                <tbody>
                <tr className={"basket-grid-item"}>
                    <th className={"td_item"}>Product</th>
                    <th className={"td_item"}>Quantity</th>
                    <th className={"td_item"}>Bid Price</th>
                    <th className={"td_item"}>State</th>
                    <th></th>
                </tr>
                {this.props.items.map((item, index )=>
                    <tr className={"basket-grid-item"} >
                        <td className={"td_item"}>{item.item}</td>
                        <td  className={"td_item"}><text> {"x "+ item.amount} </text></td>
                        <td className={"td_item"}>{item.bidPrice}</td>
                        <td className={"td_item"}>{getStates(item)}</td>
                        <td className={"td_item"}>
                            <button className={"button-6-delete"} onClick={() => this.props.onDelete(item)}>
                                <AiOutlineDelete/>
                            </button>
                            { item.isApproved ?
                                <button onClick={() => handleAddBidToCart(item) }>Add To Cart</button> :
                                <EditBidCostumerPopup  key={index} bid={item}/>
                            }
                        </td>
                    </tr>
                )}
                </tbody>
            </table>:
                <table className={"basket-grid"} >
                    <tbody>
                    <tr className={"basket-grid-item"}>
                        <th className={"td_item"}>Product</th>
                        <th className={"td_item"}>Quantity</th>
                        <th className={"td_item"}>Bid Price</th>
                        <th></th>
                    </tr>
                    {this.props.items.map((item, index )=>
                        <tr className={"basket-grid-item"} >
                            <td className={"td_item"}>{item.item}</td>
                            <td  className={"td_item"}><text> {"x "+ item.amount} </text></td>
                            <td className={"td_item"}>{item.bidPrice}</td>
                            <td className={"td_item"}>
                                <button className={"button-6-delete"} onClick={() => this.props.onDelete(item)}>
                                    <AiOutlineDelete/>
                                </button>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
        )
    };
    render() {
        return (
            <React.Fragment>
                {this.renderItems()}
            </React.Fragment>
        );
    }
}
let handleAddBidToCart = (bid) => {
    console.log("maaaaaa");
    stompClient.send("/app/market/bid/addBidToCart", {}, JSON.stringify({"bid_id" : bid.id}));
};
let getStates = (item) => {
        return item.isApproved ?
            "Approved" : "Pending"
    };
let handleReject = (item) => {
    stompClient.send("/app/market/bid/deleteBid", {}, JSON.stringify({
        "store_id": item.storeId,
        "bid_id": item.id
    }));

};

export default Bids;