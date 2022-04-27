package DomainLayer.Stores;

import DomainLayer.Response;
import DomainLayer.Users.User;
import Exceptions.LogException;

import java.util.*;
import java.util.stream.Collectors;

public class StoreController {

    private static StoreController storeController;

    private Map<Integer, Store> stores; // store-id , stores

    private static int NEXT_STORE_ID = 1;

    private StoreController() {
        this.stores = new HashMap<>();
    }


    private static class StoreControllerHolder {
        static final StoreController instance = new StoreController();
    }

    public static StoreController getInstance() {
        return StoreControllerHolder.instance;
    }

    private synchronized static int getNewStoreId() {
        return NEXT_STORE_ID++;
    }

    public Store createStore(User owner, String store_name) {
        Store store = new Store(owner.getName(), store_name, getNewStoreId());
        addStore(store);
        return store;
    }

    private void addStore(Store store) {
        this.stores.put(store.getStoreId(), store);
    }

    public Store getStore(int id) {
        return stores.getOrDefault(id, null);
    }

    public Collection<Store> getAllStores() {
        return stores.values();
    }

    public Set<Item> getItemsWithNameContains(String name) {
        return stores.values().stream().map(s -> s.getItemsWithNameContains(name)).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithKeyWord(List<String> kws) {
        return stores.values().stream().map(s -> s.getItemsWithKeyWords(kws)).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithCategory(String categoryString) throws IllegalArgumentException {
        try {
            Category category = Category.valueOf(categoryString);
            return stores.values().stream().map(s -> s.getItemsWithCategory(category)).flatMap(Set::stream).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new IllegalArgumentException("Category dose not exist");
        }
    }


    public Item addItemToStore(User manager, int storeId, String name, String category, double price, int amount) {
        if (!manager.isLoggedIn()) {
            throw new IllegalArgumentException("Use has to be logged in to do this action.");
        }
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id: %s", storeId));
        }
        if (!s.canManageItems(manager.getName())) {
            throw new LogException("You cannot add items to this store", String.format("User %s tried to add an item to a store that they do not manager", manager));
        }
        Item i = new Item(name, Category.valueOf(category), price);
        s.addItem(i, amount);
        return i;
    }

    public Item getAndDeductItemFromStore(int storeId, int itemId, int amount) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        Item i = s.getItemAndDeduct(itemId, amount);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        return i;
    }

    public Item getItemFromStore(int storeId, int itemId) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        return i;
    }

    public boolean addManager(User owner, String manager, int storeId) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isLoggedIn() || !s.canAssignManager(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign a manager");
        }
        if (!s.canBecomeManager(manager)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a manager of the store with store id %s", manager, storeId));
        }
        s.addManager(owner.getName(), manager);
        return true;
    }

    public boolean addOwner(User owner, String newOwner, int storeId) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isLoggedIn() || !s.canAssignOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign another user to be an owner");
        }
        if (!s.canBecomeOwner(newOwner)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a an owner of the store with store id %s", newOwner, storeId));
        }
        s.addOwner(owner.getName(), newOwner);
        return true;
    }

    public Item returnItemToStore(int storeId, Item item, int amount) {
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id: %s", storeId));
        }
        s.addItem(item, amount);
        return item;
    }

    public boolean isShopOwner(Store store, String shopOwnerName){
        return stores.get(store.getStoreId()).isOwner(shopOwnerName);
    }

    public void setManagerPermission(User owner, String manager, int storeId, byte permission) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isLoggedIn() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign a manager");
        }
        s.changePermission(manager, permission);
    }

    public static int getNextStoreId() {
        return NEXT_STORE_ID;
    }

    public void closeStore(String username, int storeId){
        stores.get(storeId).setIsOpen(username, false);
    }
}
