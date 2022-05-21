package ServiceLayer.DTOs;

import DomainLayer.Stores.Category;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;

import java.util.Date;

public class ItemHistoryDTO {
    public int store_id;
    public String username;
    public String product_name;
    public double price_per_unit;
    public int amount;
    public Date date;

    public ItemHistoryDTO(ItemHistory item) {
        this.product_name = item.product_name;
        this.amount = item.amount;
        this.username = item.username;
        this.price_per_unit = item.price_per_unit;
        this.store_id = item.store_id;
        this.date = item.date;
    }

    public ItemHistoryDTO() {

    }
}
