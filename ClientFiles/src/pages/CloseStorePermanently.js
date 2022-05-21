import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import ResultLabel from "../Components/ResultLabel";
import AreYouSureModal from "../Components/AreYouSureModal";

let store_to_close_id = -1;

function StoreToClose(props) {
    let [modalOpen, setModalOpen] = useState(false);

    const store = props.store;

    const handleCloseStore = () => {
        stompClient.send("/app/market/closeStorePermanently", {}, JSON.stringify({"storeId" : store.id}));
        store_to_close_id = store.id;
    }

    return (
        <div>
            <article onClick={() => {setModalOpen(true)}} key={store.id} className={"items-grid"}>
                <div>
                    <h1 className={"white-color"}>{store.name}</h1>
                </div>
            </article>

            {modalOpen && <AreYouSureModal
                title="Are You Sure?"
                body="This action is not a reversible"
                doActionButton="I'm Sure"
                regretActionButton="Cancel"
                setOpenModal={setModalOpen}
                onContinue={handleCloseStore}/>}
        </div>
    );
}


export default class CloseStorePermanentlyPage extends Component{

    constructor() {
        super();
        this.state = {
            liststores: [],
            message: "",
            hadError: false
        };
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStoresBesidesPermanentlyClosedResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.liststores = response.object
                this.setState({[this.state.liststores]: this.state.liststores});
                console.log(this.state.liststores);
            }
        });
        stompClient.send("/app/market/getStoresBesidesPermanentlyClosed", {}, JSON.stringify({}));

        await connectedPromise;
        stompClient.subscribe('/user/topic/closeStorePermanentlyResult', (r) => {
            let res = JSON.parse(r["body"]);
            if(res.errorMessage)
            {
                this.state.message = res.errorMessage;
                this.state.hadError = true;
            }
            else {
                if (res.object) {
                    this.state.hadError = false;
                    this.state.message = "Store Closed successfully";

                    this.state.liststores = this.state.liststores.filter(s => s.id !== store_to_close_id);
                    this.setState({[this.state.liststores]: this.state.liststores});
                }
                else {
                    this.state.hadError = true;
                    this.state.message = "Could not close the store successfully";
                }
            }
            this.setState({[this.state.hadError]: this.state.hadError});
            this.setState({[this.state.message]: this.state.message});
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresBesidesPermanentlyClosedResult');
        stompClient.unsubscribe('/user/topic/closeStorePermanentlyResult');
    }

    render() {
        return (
            <React.Fragment>
                <div className="formCenter">
                    <h1>Choose store to close permanently</h1>
                </div>
                <div className="store-grid-container">
                    {this.state.liststores.map((store) => (

                        <StoreToClose
                            key={store.id}
                            store = {store}
                        />
                    ))}
                </div>
                <ResultLabel text={this.state.message} hadError={this.state.hadError}/>
            </React.Fragment>
        );
    }
}

