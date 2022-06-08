package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShoppingBasketDTO {
    public int Store_id;
    public String Store_name;
    public List<BasketItemDTO> items;

    public ShoppingBasketDTO(ShoppingBasket basket, Map<Item, Double> discounts, String store_name) {
        Store_id = basket.getStoreId();
        items = new LinkedList<>();
        this.Store_name = store_name;
        for (Map.Entry<Item, Integer> item_amount : basket.getItemsAndAmounts()) {
            BasketItemDTO item = new BasketItemDTO(item_amount.getKey(), item_amount.getValue(), basket.calculatePrice(discounts, item_amount.getKey()));
            Integer amount = item_amount.getValue();
            items.add(item);
        }
    }
    public ShoppingBasketDTO(ShoppingBasket basket) {
        Store_id = basket.getStoreId();
        items = new LinkedList<>();
        for (Map.Entry<Item, Integer> item_amount : basket.getItemsAndAmounts()) {
            BasketItemDTO item = new BasketItemDTO(item_amount.getKey(), item_amount.getValue(), item_amount.getKey().getPrice());
            Integer amount = item_amount.getValue();
            items.add(item);
        }
    }

    public ShoppingBasketDTO() {

    }
}
