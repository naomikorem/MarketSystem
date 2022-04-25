package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBasket {
    private int storeId;
    private List<Item> items;

    public ShoppingBasket(int storeId) {
        this.storeId = storeId;
        this.items = new ArrayList<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

}
