package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;

import java.util.List;

public class ItemDTO {
    public int item_id;
    public String product_name;
    public String category;
    public double rate;
    public int numberOfRatings;
    public double price;
    public int amount;
    public List<String> keyWords;

    public ItemDTO(Item item, int amount) {
        this.item_id = item.getId();
        this.rate = item.getRate();
        this.price = item.getPrice();
        this.product_name = item.getProductName();
        this.category = item.getCategory().toString();
        this.keyWords = item.getKeyWords();
        this.amount = amount;
    }

    public ItemDTO() {

    }
}
