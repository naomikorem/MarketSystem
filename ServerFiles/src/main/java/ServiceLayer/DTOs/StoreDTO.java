package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreDTO
{
    public String name;
    public boolean isOpen;
    public boolean permanentlyClosed;
    public int id;
    public String founder;
    public List<String> owners;
    public List<String> managers;
    public Map<ItemDTO, Integer> items;

    public StoreDTO()
    {
        this.owners = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.items = new HashMap<>();
    }
}
