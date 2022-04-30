package DomainLayer.SystemManagement.HistoryManagement;

import DomainLayer.Stores.Item;

import java.util.*;

public class History {
    public final Set<ItemHistory> items;

    public History()
    {
        this.items = new HashSet<>();
    }

    public void addToHistory(Set<Map.Entry<Item, Integer>> itemsAndAmounts, int storeId, String username, Date purchase_date)
    {
        for (Map.Entry<Item, Integer> entry : itemsAndAmounts)
        {
            int amount = entry.getValue();
            Item item = entry.getKey();
            items.add(new ItemHistory(item.getId(), storeId, username, item.getProductName(), item.getCategory(), item.getPrice(), amount, purchase_date));
        }
    }

    public Set<ItemHistory> getHistoryItems()
    {
        return this.items;
    }
}
