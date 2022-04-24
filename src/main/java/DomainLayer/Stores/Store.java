package DomainLayer.Stores;

import Utility.LogUtility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Store {
    private String name;
    private boolean open;
    private int id;
    private String founder;
    private Map<String, String> owners;
    private Map<String, Permission> managers;
    private Map<Item, Integer> items;

    public Store(String founder, String store_name, int id) {
        this.founder = founder;
        this.name = store_name;
        this.open = true;
        this.id = id;
        this.owners = new HashMap<>();
        this.managers = new HashMap<>();
        this.items = new HashMap<>();
        owners.put(founder, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String store_name) {
        if (store_name == null || store_name.equals("")) {
            LogUtility.error("tried to change store name to an empty word / null");
            throw new IllegalArgumentException("Store name must be a non empty name");
        }
        this.name = store_name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setIsOpen(boolean is_open) {
        this.open = is_open;
    }

    public int getStoreId() {
        return id;
    }

    public boolean canBecomeManager(String user) {
        return !managers.containsKey(user);
    }

    public void addManager(String givenBy, String manager) {
        this.managers.put(manager, new Permission(givenBy));
    }

    public Set<Item> getItemsWithNameContains(String name){
        return items.keySet().stream().filter((x)->x.isNameContains(name)).collect(Collectors.toSet());
    }

}
