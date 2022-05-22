import React, {Component} from "react";
import "../App.css";

class BasketItem extends Component {
    formatCount() {

        const {amount} = this.props.item;
        return amount === 0 ? 'Zero' : amount;
    }

    render() {
        console.log('props', this.props);
        return (
            <React.Fragment>
                <div className={"basket-grid-item"}>
                    <p>{this.props.item.product_name}</p>
                    <p>{this.props.item.price}</p>
                    <p>{"X "+this.formatCount()}</p>
                    <button className={"button-6"} onClick={() => this.props.onIncrement(this.props.item)}>
                        +1
                    </button>
                    <button className={"button-6"} onClick={() => this.props.onDecrement(this.props.item)}>
                        -1
                    </button>
                    <button className={"button-6-delete"} onClick={() => this.props.onDelete(this.props.item)}>
                        Delete
                    </button>
                </div>
            </React.Fragment>
        );
    }
}

export default BasketItem;