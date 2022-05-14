package DomainLayer.SystemManagement.HistoryManagement;

import DomainLayer.Stores.Item;

import java.util.*;

public class History
{
    private final Set<ItemHistory> items;

    public History()
    {
        this.items = new HashSet<>();
    }

    /***
     * Adding items from last purchase to the current history of the store/user
     * @param itemsAndAmounts map of the bought items and their amounts
     * @param storeId The id of the store that contains those items
     * @param username The username of the user that made the last purchase
     * @param purchase_date The date of the purchase
     */
    public void addToHistory(Set<Map.Entry<Item, Integer>> itemsAndAmounts, int storeId, String username, Date purchase_date)
    {
        for (Map.Entry<Item, Integer> entry : itemsAndAmounts)
        {
            // add every item to the items list with new fields: store id, user that purchased the item, amount bought from the item and purchase date
            int amount = entry.getValue();
            Item item = entry.getKey();
            items.add(new ItemHistory(item.getId(), storeId, username, item.getProductName(), item.getCategory(), item.getPrice(), amount, purchase_date));
        }
    }

    /***
     * Return the items in the history
     * @return Set of items
     */
    public Set<ItemHistory> getHistoryItems()
    {
        return this.items;
    }
}
