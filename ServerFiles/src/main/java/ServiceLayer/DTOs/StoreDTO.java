package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Users.User;

import java.util.List;
import java.util.Map;

public class StoreDTO
{
    public String name;
    public boolean isOpen;
    public int id;
    public String founder;
    public List<String> owners;
    public List<String> managers;
    public Map<ItemDTO, Integer> items;
}
