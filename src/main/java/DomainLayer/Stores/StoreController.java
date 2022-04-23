package DomainLayer.Stores;

import DomainLayer.Users.User;

import java.util.HashMap;
import java.util.Map;

public class StoreController {

    private static StoreController storeController;

    private Map<Integer, Store> stores; // store-id , stores

    private int NEXT_STORE_ID = 1;

    private StoreController() {
        this.stores = new HashMap<>();
    }

    public static StoreController getInstance() {
        if (storeController == null) {
            storeController = new StoreController();
        }
        return storeController;
    }

    private int getNewStoreId() {
        return this.NEXT_STORE_ID++;
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

}
