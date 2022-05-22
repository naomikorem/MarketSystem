import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import ResultLabel from "../Components/ResultLabel";
import {useLocation, useParams} from "react-router-dom";

class StorePurchaseHistoryItem extends Component {

    constructor() {
        super();
    }

    render() {
        const history_item = this.props.item;

        return (
            <article className={"items-grid"}>
                <div>
                    <h3>{history_item.product_name}</h3>
                    <p>
                        Username that purchased the item: {history_item.username}<br/>
                        Bought amount: {history_item.amount}<br/>
                        Price Per Unit: {history_item.price_per_unit} <br/>
                        Purchase Date: {new Date(history_item.date).toLocaleDateString("en-GB")}
                    </p>
                </div>
            </article>
        );
    }
}

export class StoreHistoryPage extends Component{

    constructor(props) {
        super(props);
        this.state = {
            store_id: props.storeid,
            store_name: props.name,
            history_items: [],
            error: ""
        };

    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoreHistoryResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.errorMessage) {
                this.state.history_items = response.object.items
                this.setState({[this.state.history_items]: this.state.history_items});
                console.log(this.state.history_items);

            }
            else
            {
                this.state.error = response.errorMessage;
                this.setState({[this.state.error]: this.state.error});
            }
        });
        stompClient.send("/app/market/getStoreHistory", {}, JSON.stringify({"storeId" : this.state.store_id}));
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoreHistoryResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">This is the Purchase History of the store {this.state.store_name}</h1>
                </div>

                <div className="store-grid-container">
                    {this.state.history_items.map((item) => (

                        <StorePurchaseHistoryItem
                            key={item.id}
                            item = {item}
                        />
                    ))}
                </div>
                <ResultLabel text={this.state.error} hadError={this.state.error != ""}/>
            </React.Fragment>
        );
    }
}

function wrapRender() {

    let {storeid} = useParams();
    const location = useLocation()
    const { storeName } = location.state

    return <div>
        <StoreHistoryPage name={storeName} storeid={storeid}/>
    </div>
}

export default wrapRender;

