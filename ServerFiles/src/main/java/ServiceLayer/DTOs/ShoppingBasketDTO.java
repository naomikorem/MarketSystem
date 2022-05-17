package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;

import java.util.Map;

public class ShoppingBasketDTO
{
    public int Store_id;
    public Map<ItemDTO, Integer> items_and_amounts;
}
