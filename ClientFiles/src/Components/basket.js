import React, {Component} from "react";
import BasketItem from './basketItem'

class Basket extends Component{
    renderItems = () => {
        if(this.props.basket.items == null || this.props.basket.items.length === 0) return <p>Your basket is empty</p>;
        return (
            <ul>
                {this.props.basket.items.map(item =>
                    <li key={"li+"+item.id}>
                        <BasketItem
                            key={item.id}
                            item={item}
                            onDelete={(item) => this.props.onDelete(this.props.basket, item)}
                            onIncrement={(item) => this.props.onIncrement(this.props.basket, item)}
                        />
                    </li>)}
            </ul>)
    };
    render() {
        return (
            <React.Fragment>
                <h1>{this.props.basket.storeName}</h1>
                {this.renderItems()}
            </React.Fragment>
        );
    }
}

export default Basket;