package DomainLayer.SystemManagement.HistoryManagement;

import DomainLayer.Stores.Category;

import java.util.Date;

public class ItemHistory
{
    public final int id;
    public final int store_id;
    public final String username;
    public final String product_name;
    public final Category category;
    public final double price_per_unit;
    public final int amount;
    public final Date date;

    public ItemHistory(int id, int store_id, String username, String product_name, Category category, double price_per_unit, int amount, Date date)
    {
        this.id = id;
        this.username = username;
        this.store_id = store_id;
        this.product_name = product_name;
        this.category = category;
        this.price_per_unit = price_per_unit;
        this.amount = amount;
        this.date = date;
    }
}
