package ServiceLayer.DTOs;

import DomainLayer.Stores.Category;

import java.util.Date;

public class ItemHistoryDTO
{
    public int item_id;
    public int store_id;
    public String username;
    public String product_name;
    public double price_per_unit;
    public int amount;
    public Date date;
}
