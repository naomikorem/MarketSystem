import React, {Component} from "react";
import BasketItem from './basketItem'
import "../App.css" ;

class Basket extends Component{
    renderItems = () => {
        if(this.props.items == null || this.props.items.length === 0) return <p>Your basket is empty</p>;
        return (
            <table className={"basket-grid"} >
                <tbody>
                <tr className={"basket-grid-item"}>
                    <th className={"td_item"}>Product</th>
                    <th className={"td_item"}>Price</th>
                    <th className={"td_item"}>Quantity</th>
                    <th className={"td_item"}>Total</th>
                </tr>
                {this.props.items.map(item =>
                        <BasketItem
                            key={item.item_id}
                            item={item}
                            onDelete={(item) => this.props.onDelete(this.props.store, item)}
                            onDecrement={(item) => this.props.onDecrement(this.props.store, item)}
                            onIncrement={(item) => this.props.onIncrement(this.props.store, item)}
                        />
                   )}
                </tbody>
                </table>)
    };
    render() {
        return (
            <React.Fragment>
                <h3 className={"store-name"}>{this.props.storeName}</h3>
                {this.renderItems()}
            </React.Fragment>
        );
    }
}

export default Basket;