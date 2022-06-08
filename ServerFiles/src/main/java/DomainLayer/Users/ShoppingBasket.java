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

    public boolean hasItem(Item item) {
        return items.containsKey(item);
    }

    public boolean hasItem(int itemId) {
        return items.keySet().stream().anyMatch(i -> i.getId() == itemId);
    }

    public int amountFromItem(Item item) {
        return items.getOrDefault(item, 0);
    }


    public double calculatePrice(Map<Item, Double> discounts) {
        // 3.1 The system checks that the basket follows the purchase rules of the store's purchase policy.
        double price = 0;
        for (Item item : this.items.keySet()) {
            price += calculatePrice(discounts, item);
        }
        return price;
    }

    public double calculatePrice(Map<Item, Double> discounts, Item item) {
        return item.getPrice() * amountFromItem(item) * (1 - discounts.getOrDefault(item, 0.0));
    }

    public double calculatePrice() {
        return this.calculatePrice(new HashMap<>());
    }
}
