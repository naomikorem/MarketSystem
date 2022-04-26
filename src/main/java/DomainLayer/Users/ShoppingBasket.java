package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.*;

public class ShoppingBasket {
    private int storeId;
    private Map<Item, Integer> items;

    public ShoppingBasket(int storeId) {
        this.storeId = storeId;
        this.items = new HashMap<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public void addItem(Item item) {
        this.items.put(item, items.getOrDefault(item, 0)+1);
    }

    public void removeItem(Item item) {
        int oldAmount = this.items.getOrDefault(item, 0);
        if(oldAmount > 1)
            this.items.put(item,oldAmount - 1);
        else
            this.items.remove(item);
    }

    public Set<Item> getItems(){
        return items.keySet();
    }


}
