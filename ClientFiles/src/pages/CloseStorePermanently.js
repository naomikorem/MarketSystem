import React, {Component, useState} from "react";
import {stompClient, connectedPromise} from "../App";
import ResultLabel from "../Components/ResultLabel";
import AreYouSureModal from "../Components/AreYouSureModal";

function StoreToClose(props) {
    let [modalOpen, setModalOpen] = useState(false);

    const store = props.store;


    const handleCloseStore = () => {
        stompClient.send("/app/market/closeStorePermanently", {}, JSON.stringify({"storeId" : store.id}));
    }

    return (
        <div>
            <article onClick={() => {setModalOpen(true)}} key={store.id} className={"items-grid"}>
                <div>
                    <h1 className={"white-color"}>{store.name}</h1>
                </div>
            </article>

            {modalOpen && <AreYouSureModal
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
        stompClient.subscribe('/user/topic/getStoresResult', (r) => {
            let response = JSON.parse(r["body"]);
            if (!response.error) {
                this.state.liststores = response.object
                this.setState({[this.state.liststores]: this.state.liststores});
                console.log(this.state.liststores);
                //console.log(this.state.listitems[0].product_name)
                //console.log(this.state.listitems.map((listitem) => (listitem.)));

            }
        });
        stompClient.send("/app/market/getStores", {}, JSON.stringify({}));

        await connectedPromise;
        stompClient.subscribe('/user/topic/closeStorePermanentlyResult', (r) => {
            let res = JSON.parse(r["body"]);
            if(res.errorMessage || !res.object)
            {
                this.state.message = "Could not close the store permanently";
                this.state.hadError = true;
                this.setState({[this.state.hadError]: this.state.hadError});
            }
            else {
                this.state.message = "Closed the store successfully";
            }
            this.setState({[this.state.message]: this.state.message});
        });
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStoresResult');
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

    // renderItem(listitem) {
    //     return(
    //         <div key={listitem.id} className={"store-grid-item"}>
    //             {/*<Link to={`/store/${listitem.id}`} className="storeLink">*/}
    //             {/*    {listitem.name}*/}
    //             {/*</Link>*/}
    //             <label>
    //                 {listitem.product_name}
    //             </label>
    //         </div>);
}

