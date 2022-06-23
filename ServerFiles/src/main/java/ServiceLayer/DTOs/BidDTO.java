package ServiceLayer.DTOs;

import DomainLayer.Stores.Bid;

import java.util.List;

public class BidDTO {
    public int id;
    public int store;
    public String costumerName;
    public double bidPrice;
    public int item;
    public int amount;
    public List<String> approvedManagers;
    public Boolean isApproved;
    public Boolean inCart;

    public BidDTO(Bid bid) {
        this.id = bid.getId();
        this.store = bid.getStore();
        this.costumerName = bid.getCostumer();
        this.bidPrice = bid.getBidPrice();
        this.item = bid.getItem();
        this.amount = bid.getAmount();
        this.approvedManagers = bid.getApprovedManagers();
        this.isApproved = bid.getApproved();
        this.inCart = bid.isInCart();
    }
}
