package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


}
