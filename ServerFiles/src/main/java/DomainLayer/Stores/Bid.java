package DomainLayer.Stores;

import DomainLayer.Users.User;

import java.util.LinkedList;
import java.util.List;


public class Bid {
    int id;
    int store;
    String costumerName;
    double bidPrice;
    int item;
    int amount;
    List<String> approvedManagers;
    Boolean isApproved;
    Boolean inCart;

    public Bid(int id,int store, String costumerName, double bidPrice, int item, int amount) {
        this.id = id;
        this.store = store;
        this.costumerName = costumerName;
        this.bidPrice = bidPrice;
        this.item = item;
        this.amount = amount;
        this.approvedManagers = new LinkedList<>();
        this.isApproved = false;
        this.inCart = false;
    }

    public Boolean isInCart() {
        return inCart;
    }

    public void addToCart() {
        this.inCart = true;
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

    public List<String> getApprovedManagers() {
        return approvedManagers;
    }

    public int getId() {
        return id;
    }

    public int getStore() {
        return store;
    }
}
