import React, {Component} from "react";
import { Chart } from "react-google-charts";
import {stompClient, connectedPromise} from "../App";

class StatsCharts extends Component {

    constructor() {
        super();
        this.state = {
            stats: [],
        }
    }

    async componentDidMount() {
        await connectedPromise;
        stompClient.subscribe('/user/topic/getStatsResult', (r) => {
            const res = JSON.parse(r["body"]);
            if (!res.errorMessage) {
                this.state.stats = res.object;
                this.setState({[this.state.stats]: this.state.stats});
            }
        });
        stompClient.send("/app/market/getStats", {}, JSON.stringify({
        }));
        this.mounted = true;
    }

    componentWillUnmount() {
        stompClient.unsubscribe('/user/topic/getStatsResult');
        this.mounted = false;
    }

    render() {
        return <Chart
            chartType="BarChart"
            data={[
                ['Year', 'Guests', 'Regular Users', 'Managers', 'Owners',
                    'Admins', { role: 'annotation' } ],
            ].concat(this.state.stats.map(s => [s.date, s.guests, s.regulars, s.managers, s.owners, s.admins, '']))
            }
            options={{"title": "Users statistics", "isStacked": true}}
            width={"100%"}
            height={"400px"}
        />
    }
}

export default StatsCharts;