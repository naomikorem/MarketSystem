package DomainLayer.Stores;

import Exceptions.LogException;
import Utility.LogUtility;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Store {
    private String name;
    private boolean open;
    private int id;
    private String founder;
    private Map<String, String> owners;
    private Map<String, Permission> managers;
    private Map<Item, Integer> items;
    private Lock lock;

    public Store(String founder, String store_name, int id) {
        this.founder = founder;
        this.open = true;
        this.id = id;
        this.owners = new HashMap<>();
        this.managers = new HashMap<>();
        this.items = new HashMap<>();
        this.lock = new ReentrantLock();
        owners.put(founder, null);
        setName(store_name);
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
        if (!isOpen()) {
            LogUtility.error("tried to change store name for a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        if (store_name == null || store_name.equals("")) {
            LogUtility.error("tried to change store name to an empty word / null");
            throw new IllegalArgumentException("Store name must be a non empty name");
        }
        this.name = store_name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setIsOpen(String username, boolean is_open) {
        if (!this.founder.equals(username)) {
            LogUtility.error(String.format("someone who isn't the store founder tried to close the store store id %s", getStoreId()));
            throw new IllegalArgumentException("someone who isn't the store founder tried to close the store");
        }
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
        synchronized (lock) {
            if (!isOpen()) {
                LogUtility.error("tried to add a manger for a closed store");
                throw new IllegalArgumentException("This store is closed");
            }
            if(managers.containsKey(manager)||owners.containsKey(manager)){
                LogUtility.error("tried to add a manger who is already a store-owner/ manager");
                throw new IllegalArgumentException("This manger is already a store-owner/ manager");
            }
            this.managers.put(manager, new Permission(givenBy));
            LogUtility.info(String.format("New manager for shop "+this.id+", the store manger is :"+ manager));
        }
    }

    public void addOwner(String givenBy, String newOwner) {
        synchronized (lock) {
            if (!isOpen()) {
                LogUtility.error("tried to add owner for a closed store");
                throw new IllegalArgumentException("This store is closed");
            }
            if(managers.containsKey(newOwner)||owners.containsKey(newOwner)) {
                LogUtility.error("tried to add a manger who is already a store-owner/ manager");
                throw new IllegalArgumentException("This manger is already a store-owner/ manager");
            }
            this.owners.put(newOwner, givenBy);
            LogUtility.info(String.format("New store owner for shop "+this.id+", the store owner is :"+ newOwner));
        }
    }

    public Set<Item> getItemsWithNameContains(String name) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.isNameContains(name)).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithKeyWords(List<String> kws) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.hasKeyWords(kws)).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithCategory(Category category) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.getCategory().equals(category)).collect(Collectors.toSet());
    }

    public Item getItemById(int id) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public Item getItemAndDeduct(int itemId, int toDeduct) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
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
        if (!isOpen()) {
            LogUtility.error("tried to add item for a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        /*if(!isManager(userName) && !isOwner(userName)) {
            LogUtility.error("A user that isn't the store owner/manger tried to add items");
            throw new IllegalArgumentException("This store is closed");
        }*/
        synchronized (items) {
            this.items.put(item, items.getOrDefault(item, 0) + amount);
        }
    }

    public void changePermission(String manager, byte permission) {
        if (!isOpen()) {
            LogUtility.error("tried to change permissions for a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        Permission p = managers.getOrDefault(manager, null);
        if (p == null) {
            throw new IllegalArgumentException(String.format("%s is not a manager in the store", manager));
        }
        p.setPermissionsMask(permission);
    }

    public String getFounder(){
        return this.founder;
    }

}
