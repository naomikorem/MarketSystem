import React, {Component} from "react";
import {stompClient, connectedPromise, user} from "../App";
import StoreHistoryPage from "./StoreHistoryPage";
import ResultLabel from "../Components/ResultLabel";
import {Link} from "react-router-dom";
//
// class StorePurchaseHistoryItem extends Component {
//
//     constructor() {
//         super();
//
//
//     }
//
//     render() {
//         const history_item = this.props.item;
//
//         return (
//             <article className={"items-grid"}>
//                 <div>
//                     <h3>{history_item.product_name}</h3>
//                     <p>
//                         Bought amount: {history_item.amount}<br/>
//                         Price Per Unit: {history_item.price_per_unit} <br/>
//                         Store id: {history_item.store_id} <br/>
//                         Purchase Date: {new Date(history_item.date).toLocaleDateString("en-GB")}
//                     </p>
//                 </div>
//             </article>
//         );
//     }
// }

export default class StorePurchaseHistory extends Component{

    constructor() {
        super();
        this.state = {
            stores: []
        };


        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.liststores = response.object
                this.setState({[this.state.liststores]: this.state.liststores});
                console.log(this.state.liststores);
            }
        });
        stompClient.send("/app/market/getStores", {}, JSON.stringify({}));
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1 className="center-text">Store's Purchase History</h1>
                </div>
                <div className="formCenter">
                    <h2 className="center-text">Select store to view it's purchase history</h2>
                </div>
                <div className="store-grid-container">
                    {this.state.stores.map((store) => (

                        <StoreHistoryPage
                            key={store.id}
                            store = {store}
                        />
                    ))}
                </div>
            </React.Fragment>
        );
    }
}

