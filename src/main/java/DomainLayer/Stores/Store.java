package DomainLayer.Stores;

import Exceptions.LogException;
import Utility.LogUtility;

import java.util.*;
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
        setName(store_name);
        this.open = true;
        this.id = id;
        this.owners = new HashMap<>();
        this.managers = new HashMap<>();
        this.items = new HashMap<>();
        owners.put(founder, null);
    }

    public boolean isOwner(String name) {
        return owners.containsKey(name);
    }

    public boolean isManager(String name) {
        return managers.containsKey(name);
    }

    public boolean canManageItems(String name) {
        return isOwner(name) || (isManager(name) && managers.get(name).canChangeItems());
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

    public boolean canAssignManager(String user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canAssignManager());
    }

    public boolean canBecomeOwner(String user) {
        return !owners.containsKey(user);
    }

    public boolean canAssignOwner(String user) {
        return isOwner(user);
    }

    public void addManager(String givenBy, String manager) {
        this.managers.put(manager, new Permission(givenBy));
    }

    public void addOwner(String givenBy, String newOwner) {
        this.owners.put(newOwner, givenBy);
    }

    public Set<Item> getItemsWithNameContains(String name){
        return items.keySet().stream().filter((x)->x.isNameContains(name)).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithKeyWords(List<String> kws){
        return items.keySet().stream().filter((x)->x.hasKeyWords(kws)).collect(Collectors.toSet());
    }
    public Set<Item> getItemsWithCategory(Category category){
        return items.keySet().stream().filter((x)->x.getCategory().equals(category)).collect(Collectors.toSet());
    }

    public Item getItemById(int id) {
        return items.keySet().stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public Item getItemAndDeduct(int itemId, int toDeduct) {
        synchronized (items) {
            Item i = items.keySet().stream().filter(item -> item.getId() == itemId).findFirst().orElse(null);
            if (i != null) {
                int amount = items.getOrDefault(i, 0);
                if (amount >= toDeduct) {
                    items.put(i, amount - toDeduct);
                    return i;
                } else {
                    throw new LogException("There isn't enough item " + itemId + " in stock", "User tried to buy item " + itemId + " but there isn't enough in stock");
                }
            }
            throw new IllegalArgumentException("Could not find item id " + itemId);
        }
    }

    public void addItem(Item item, int amount) {
        synchronized (items) {
            this.items.put(item, items.getOrDefault(item, 0) + amount);
        }
    }

    public void changePermission(String manager, byte permission) {
        Permission p = managers.getOrDefault(manager, null);
        if (p == null) {
            throw new IllegalArgumentException(String.format("%s is not a manager in the store", manager));
        }
        p.setPermissionsMask(permission);
    }

}
