package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.*;

public class ShoppingBasket {
    private int storeId;
    private final Map<Item, Integer> items;

    public ShoppingBasket(int storeId) {
        this.storeId = storeId;
        this.items = new HashMap<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public void addItem(Item item, int amount) {
        this.items.put(item, items.getOrDefault(item, 0) + amount);
    }

    public void removeItem(Item item) {
        int oldAmount = this.items.getOrDefault(item, 0);
        if (oldAmount > 1)
            this.items.put(item, oldAmount - 1);
        else
            this.items.remove(item);
    }

    public Set<Item> getItems() {
        return items.keySet();
    }

    public Set<Map.Entry<Item, Integer>> getItemsAndAmounts() {
        return items.entrySet();
    }


    public double calculatePrice() {
        // 3.1 The system checks that the basket follows the purchase rules of the store's purchase policy.
        double price = 0;
        for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
            price += entry.getKey().getPrice() * entry.getValue();
        }
        return price;
    }
}
