package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class ShoppingCart {
    private Map<Integer, ShoppingBasket> shoppingBaskets;

    public ShoppingCart() {
        this.shoppingBaskets = new HashMap<>();
    }

    public void addBasket(ShoppingBasket basket) {
        shoppingBaskets.put(basket.getStoreId(), basket);
    }

    public ShoppingBasket getBasket(int storeId) {
        return shoppingBaskets.getOrDefault(storeId, null);
    }

    public List<Item> getAllItems() {
        return shoppingBaskets.values().stream().flatMap(sb -> sb.getItems().stream()).collect(Collectors.toList());
    }
    public List<ShoppingBasket> getBaskets() {
        return new ArrayList<>(shoppingBaskets.values());
    }

    public void addItem(int storeId, Item item) {
        if (!shoppingBaskets.containsKey(storeId)) {
            shoppingBaskets.put(storeId, new ShoppingBasket(storeId));
        }
        shoppingBaskets.get(storeId).addItem(item);
    }


}