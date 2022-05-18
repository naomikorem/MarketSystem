import React, {Component} from "react";


class BasketItem extends Component{
    formatCount(){
        const {amount} = this.props.item;
        return amount === 0? 'Zero' : amount;
    }
    render() {
        console.log('props', this.props);
        return (
            <React.Fragment>
                <span><h2>{this.props.item.id}</h2> </span>
                <span className="badge badge-primary m-2">{this.formatCount()}</span>
                <button onClick={() => this.props.onIncrement(this.props.item)}>
                    Add
                </button>
                <button onClick={() => this.props.onDelete(this.props.item)}>
                    Delete
                </button>

            </React.Fragment>
        );
    }
}

export default BasketItem;