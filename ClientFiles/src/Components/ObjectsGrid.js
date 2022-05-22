import React, {Component} from "react";
import {Link} from "react-router-dom";


class ObjectsGrid extends Component {


    render() { return (
        <div className="store-grid-container">
            {this.props.listitems.map((listitem, index) => (
                <div key={index} className={"store-grid-item"}>
                    <Link to={`/${this.props.link}/${listitem.id}`} state={{ storeName: listitem.name }} className="storeLink">
                        {listitem.name}
                    </Link>
                </div>
            ))}
        </div>);
    }
}

export default ObjectsGrid;