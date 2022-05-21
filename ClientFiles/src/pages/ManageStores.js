import {stompClient, connectedPromise, user} from "../App";
import React, {Component, useState} from "react";
import ObjectsGrid from "../Components/ObjectsGrid";
import ResultLabel from "../Components/ResultLabel";
import AreYouSureModal from "../Components/AreYouSureModal";

let store_to_reopen = null;

function ClosedStoreItem(props) {
    let [modalOpen, setModalOpen] = useState(false);

    const store = props.store;

    const handleReopenStore = () => {
        stompClient.send("/app/market/reopenStore", {}, JSON.stringify({"storeId" : store.id}));
        store_to_reopen = store;
    }

    return (
        <div>
            <article onClick={() => {setModalOpen(true)}} key={store.id} className={"items-grid"}>
                <div>
                    <h1 className={"white-color"}>{store.name}</h1>
                </div>
            </article>

            {modalOpen && <AreYouSureModal
                title={store.name}
                body="Do you want to reopen this store?"
                doActionButton="Yes"
                regretActionButton="No"
                setOpenModal={setModalOpen}
                onContinue={handleReopenStore}/>}
        </div>
    );
}


class ManageStores extends Component {
    constructor(props) {
        super(props);
        this.state = {
            open_store_items: [],
            closed_store_items: [],
            message: "",
            hadError: false
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getUsersStoresResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (res.errorMessage == null)
            {
                console.log(res.object)

                this.state.message = "";
                this.state.hadError = false;

                this.state.open_store_items = res.object.filter(s => s.isOpen);
                this.setState({[this.state.open_store_items]: this.state.open_store_items});
                this.state.closed_store_items = res.object.filter(s => !s.isOpen && !s.permanentlyClosed);
                this.setState({[this.state.closed_store_items]: this.state.closed_store_items});
            }
            else
            {
                this.state.hadError = true;
                this.state.message = res.errorMessage;
            }
            this.setState({[this.state.message]: this.state.message});
        });

        stompClient.subscribe('/user/topic/reopenStoreResult', (r) => {
            let res = JSON.parse(r["body"]);
            if(res.errorMessage)
            {
                this.state.message = res.errorMessage;
                this.state.hadError = true;
            }
            else {
                if (res.object) {
                    this.state.hadError = false;
                    this.state.message = "Store reopened successfully";

                    this.state.closed_store_items = this.state.closed_store_items.filter(s => store_to_reopen == null || s.id !== store_to_reopen.id);
                    this.state.open_store_items.push(store_to_reopen)
                    this.setState({[this.state.open_store_items]: this.state.open_store_items});
                    this.setState({[this.state.closed_store_items]: this.state.closed_store_items});
                }
                else {
                    this.state.hadError = true;
                    this.state.message = "Could not reopen the store successfully";
                }
            }
            this.setState({[this.state.hadError]: this.state.hadError});
            this.setState({[this.state.message]: this.state.message});
        });

        stompClient.send("/app/market/getUsersStores", {}, {});
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
    }

    render() {
        return (
            <div>
                <h1 className="center-text">Hello {user ? user.userName : "Guest"}, Choose a store to manage</h1>
                <div>
                    <h4>Your Open Stores</h4>
                    <div className="store-grid-container">
                        <ObjectsGrid listitems={this.state.open_store_items} link={"edit-store"}/>
                    </div>
                </div>
                <div>
                    <h4>Your Closed Stores</h4>
                    <div className="store-grid-container">
                        {this.state.closed_store_items.map((store) => (

                            <ClosedStoreItem
                                key={store.id}
                                store = {store}
                            />
                        ))}

                    </div>
                </div>
                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </div>

        );
    }
}

function wrapRender() {
    return <div>
        <ManageStores/>
    </div>
}

export default wrapRender;
