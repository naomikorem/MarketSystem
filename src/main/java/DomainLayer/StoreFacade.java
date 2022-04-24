package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.Set;

public class StoreFacade {

    private StoreController storeController;

    public StoreFacade() {
        this.storeController = StoreController.getInstance();
    }

    public void addNewStore(User owner , String storeName) {
        if (!owner.isLoggedIn()) {
            throw new RuntimeException("The user is not logged in.");
        }
        storeController.createStore(owner, storeName);
    }

    public void addManager(User owner, String manager, int storeId) {
        Store s = storeController.getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!s.canBecomeManager(manager)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a manager of the store with store id %s", manager, storeId));
        }
        s.addManager(owner.getName(), manager);
    }

    public Response<Collection<Store>> getAllStores(){
        return new Response<>(storeController.getAllStores());
    }
    public Response<Store> getStore(int id){
        return new Response<>(storeController.getStore(id));
    }
    public Response<Set<Item>> getItemsWithNameContains(String name) {
        return new Response<>(storeController.getItemsWithNameContains(name));
    }
}