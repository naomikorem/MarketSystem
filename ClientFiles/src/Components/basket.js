import React, {Component} from "react";
import BasketItem from './basketItem'
import "../App.css";

class Basket extends Component{
    renderItems = () => {
        if(this.props.items == null || this.props.items.length === 0) return <p>Your basket is empty</p>;
        return (
            <ul style ={{listStyle:'none'}} >
                {this.props.items.map(item =>
                {console.log("hi: "+ item);
                    return (
                    <li key={"li+"+item.item_id} >
                        <BasketItem
                            key={item.item_id}
                            item={item}
                            onDelete={(item) => this.props.onDelete(this.props.store, item)}
                            onDecrement={(item) => this.props.onDecrement(this.props.store, item)}
                            onIncrement={(item) => this.props.onIncrement(this.props.store, item)}
                        />
                    </li>);})}
            </ul>)
    };
    render() {
        return (
            <React.Fragment>
                <h3 className={"store-name"}>{this.props.store}</h3>
                {this.renderItems()}
            </React.Fragment>
        );
    }
}

export default Basket;