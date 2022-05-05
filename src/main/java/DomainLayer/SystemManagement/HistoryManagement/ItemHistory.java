package DomainLayer.SystemManagement.HistoryManagement;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object item)
    {
        if(item instanceof ItemHistory)
        {
            ItemHistory i = (ItemHistory)item;
            return i.price_per_unit == this.price_per_unit &&
                    i.product_name.equals(this.product_name) &&
                    i.id == this.id &&
                    i.store_id == this.store_id &&
                    i.amount == this.amount &&

                    i.category == this.category;

        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, store_id, username, product_name, category, price_per_unit, amount);

    }
}
