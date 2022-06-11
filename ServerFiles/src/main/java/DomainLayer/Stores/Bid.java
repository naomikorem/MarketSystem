package DomainLayer.Stores;

import DomainLayer.Users.User;

import java.util.LinkedList;
import java.util.List;


public class Bid {
    int id;
    String costumerName;
    Double bidPrice;
    int item;
    int amount;
    List<String> approvedManagers;
    Boolean isApproved;

    public Bid(int id, String costumerName, Double bidPrice, int item, int amount) {
        this.id = id;
        this.costumerName = costumerName;
        this.bidPrice = bidPrice;
        this.item = item;
        this.amount = amount;
        this.approvedManagers = new LinkedList<>();
        this.isApproved = false;
    }

    public void setBidPrice(Double bidPrice) {
        this.approvedManagers.clear();
        this.bidPrice = bidPrice;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getCostumer() {
        return costumerName;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public int getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void approve(String userName) {
        approvedManagers.add(userName);
    }

    public int getId() {
        return id;
    }
}
