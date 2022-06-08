package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;

import java.util.List;

public class BasketItemDTO {
    public int item_id;
    public String product_name;
    public double price;
    public int amount;
    public double discounted_price;

    public BasketItemDTO(Item item, int amount, double newPrice) {
        this.item_id = item.getId();
        this.price = item.getPrice();
        this.product_name = item.getProductName();
        this.amount = amount;
        this.discounted_price = newPrice;
    }
}
