package main.java.DomainLayer.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


}
