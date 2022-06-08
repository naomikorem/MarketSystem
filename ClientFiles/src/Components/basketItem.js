import React, {Component} from "react";
import "../App.css";
import {AiOutlineDelete} from "react-icons/all";

class BasketItem extends Component {
    formatCount() {

        const {amount} = this.props.item;
        return amount === 0 ? 'Zero' : amount;
    }

    render() {
        console.log('props', this.props);
        return (
            <tr className={"basket-grid-item"} >
                    <td className={"td_item"}>{this.props.item.product_name}</td>

                    <td className={"td_item"}>
                        {((this.props.item.price * this.props.item.amount) === this.props.item.discounted_price) ?
                            this.props.item.price :
                            <p><s>{this.props.item.price}<br/></s>
                            {this.props.item.discounted_price / this.props.item.amount}</p>
                        }
                    </td>
                    <td  className={"td_item"}>
                        <div style={{backgroundColor: "white", display: "inline", borderRadius: "10%", paddingBlock: "8px", marginRight: "5px"}}>
                            <button className={"button-6"} onClick={() => this.props.onDecrement(this.props.item)}>
                                -
                            </button>
                            <text style={{color: "black"}}> {"x "+this.formatCount()} </text>
                            <button className={"button-6"} onClick={() => this.props.onIncrement(this.props.item)}>
                                +
                            </button>
                        </div>
                    <button className={"button-6-delete"} onClick={() => this.props.onDelete(this.props.item)}>
                        <AiOutlineDelete/>
                    </button>
                    </td>
                <td className={"td_item"}>{this.props.item.discounted_price}</td>
            </tr>
        );
    }
}

export default BasketItem;