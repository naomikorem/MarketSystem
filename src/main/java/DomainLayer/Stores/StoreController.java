package DomainLayer.Stores;

import DomainLayer.Users.User;
import Exceptions.LogException;
import Utility.LogUtility;

import java.util.*;
import java.util.stream.Collectors;

public class StoreController {

    private Map<Integer, Store> stores; // store-id , stores

    private int NEXT_STORE_ID = 1;

    private StoreController() {
        this.stores = new HashMap<>();
    }


    private static class StoreControllerHolder {
        static final StoreController instance = new StoreController();
    }

    public static StoreController getInstance() {
        return StoreControllerHolder.instance;
    }

    private synchronized int getNewStoreId() {
        return NEXT_STORE_ID++;
    }

    public Store createStore(User owner, String store_name) {
        Store store = new Store(owner, store_name, getNewStoreId());
        addStore(store);
        return store;
    }

    public Boolean isExist(int storeId) { return this.stores.getOrDefault(storeId, null)!=null;}
    private void addStore(Store store) {
        this.stores.put(store.getStoreId(), store);
    }
    /**for debugging**/
    public void removeStore(Store store) {
        this.stores.remove(store.getStoreId());
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
        if (!manager.isSubscribed()) {
            throw new IllegalArgumentException("Use has to be logged in to do this action.");
        }
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id: %s", storeId));
        }
        if (!s.canManageItems(manager)) {
            throw new LogException("You cannot add items to this store", String.format("User %s tried to add an item to a store that they do not manager", manager));
        }
        Item i = new Item(name, Category.valueOf(category), price);
        s.addItem(i, amount);
        LogUtility.info(String.format("%s added %s %s to store id %s", manager.getName(), amount, name, storeId));
        return i;
    }

    public Item reserveItemFromStore(int storeId, int itemId, int amount) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        Item i = s.getItemAndDeduct(itemId, amount);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        LogUtility.info(String.format("%s %s were reserved in store %s", amount, i.getProductName(), storeId));
        return i;
    }

    public Item removeItemFromStore(User owner, int storeId, int itemId, int amount) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed() || !s.canManageItems(owner)) {
            throw new LogException("You cannot remove items from this store.", String.format("a user tried to illegally edit store %s", storeId));
        }
        Item i = s.getItemAndDeduct(itemId, amount);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        LogUtility.info(String.format("%s %s were taken out of store %s by %s", amount, i.getProductName(), storeId, owner.getName()));
        return i;
    }

    public Item getItemFromStore(int storeId, int itemId) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        return i;
    }

    public boolean addManager(User owner, User manager, int storeId) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed() || !s.canAssignManager(owner)) {
            throw new IllegalArgumentException("This user cannot assign a manager");
        }
        if (!s.canBecomeManager(manager)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a manager of the store with store id %s", manager, storeId));
        }
        s.addManager(owner.getName(), manager);
        LogUtility.info(String.format("%s assigned %s as a manager at store %s", owner.getName(), manager, storeId));
        return true;
    }

    public boolean addOwner(User owner, User newOwner, int storeId) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed() || !s.canAssignOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign another user to be an owner");
        }
        if (!s.canBecomeOwner(newOwner)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a an owner of the store with store id %s", newOwner, storeId));
        }
        s.addOwner(owner.getName(), newOwner);
        LogUtility.info(String.format("%s assigned %s as an owner at store %s", owner.getName(), newOwner, storeId));
        return true;
    }

    public Item returnItemToStore(int storeId, Item item, int amount) {
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id: %s", storeId));
        }
        s.addItem(item, amount);
        LogUtility.info(String.format("%s %s were returned to store %s", amount, item.getProductName(), storeId));
        return item;
    }

    public boolean isShopOwner(Store store, String shopOwnerName) {
        return stores.get(store.getStoreId()).isOwner(shopOwnerName);
    }

    public void setManagerPermission(User owner, String manager, int storeId, byte permission) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign manager's permission");
        }
        s.changePermission(manager, permission);
        LogUtility.info(String.format("%s changed the permissions of manager %s", owner.getName(), manager));
    }

    public void closeStore(User user, int storeId) {
        if (!isExist(storeId)) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!user.isSubscribed()) {
            throw new IllegalArgumentException("Only logged in users can perform this action.");
        }
        stores.get(storeId).setIsOpen(user.getName(), false);
        LogUtility.info(String.format("User %s just closed store %s", user.getName(), storeId));
    }

    public void permanentlyCloseStore(int storeId) {
        if (!isExist(storeId)) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        stores.get(storeId).setPermanentlyClosed(true);
        LogUtility.info(String.format("store %s was permanently closed by an admin", storeId));
    }

    public void removeOwner(User owner, User toRemove, int storeId) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed()) {
            throw new IllegalArgumentException("Guest users can not perform this action.");
        }
        s.removeStoreOwner(owner.getName(), toRemove);
        LogUtility.info(String.format("%s removed %s from being a store owner at store %s", owner.getName(), toRemove, storeId));
    }

    public void removeManager(User owner, User toRemove, int storeId) {
        Store s = getInstance().getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed()) {
            throw new IllegalArgumentException("Guest users can not perform this action.");
        }
        s.removeStoreManager(owner.getName(), toRemove);
        LogUtility.info(String.format("%s removed %s from being a store manager at store %s", owner.getName(), toRemove, storeId));
    }

    public void removeUserRoles(User owner, User removed) {
        for (int id : removed.getManagedStores()) {
            Store s = getStore(id);
            s.removeStoreManager(owner.getName(), removed);
        }
        for (int id : removed.getOwnedStores()) {
            Store s = getStore(id);
            s.removeStoreOwner(owner.getName(), removed);
        }
    }

    public Item modifyItem(User owner, int storeId, int itemId, String productName, String category, double price, List<String> keywords) {
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with id %s in store %s", itemId, storeId));
        }
        if (!owner.isSubscribed() || !s.canManageItems(owner)) {
            throw new IllegalArgumentException("Only store owners can perform this action.");
        }
        i.updateItem(productName, Category.valueOf(category), price, keywords);
        return i;
    }

    public Map<Item, Integer> getItems(int storeId) {
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        return s.getItems();
    }
      
      
    public List<String> getManagers(User owner, int storeId){
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        List<String> result = s.getManagers();
        LogUtility.info(String.format("%s got list of managers: %s from store %s", owner.getName(), result, storeId));
        return result;
    }

    public Set<Item> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating){
        Set<Item> output = new HashSet<>();
        for (Item item: items) {
            if(upLimit == -1 || item.getPrice() <= upLimit)
                if(lowLimit == -1 || item.getPrice() >= lowLimit)
                    if(rating== -1 || item.getRate() > rating)
                        output.add(item);
        }
        return output;
    }
}
