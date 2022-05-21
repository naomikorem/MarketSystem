package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShoppingBasketDTO {
    public int Store_id;
    public List<ItemDTO> items;

    public ShoppingBasketDTO(ShoppingBasket basket) {
        Store_id = basket.getStoreId();
        items = new LinkedList<>();
        for (Map.Entry<Item, Integer> item_amount : basket.getItemsAndAmounts()) {
            ItemDTO item = new ItemDTO(item_amount.getKey(), item_amount.getValue());
            Integer amount = item_amount.getValue();
            items.add(item);
        }
    }

    public ShoppingBasketDTO() {

    }
}
