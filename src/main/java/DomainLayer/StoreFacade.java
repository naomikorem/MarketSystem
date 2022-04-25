package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StoreFacade {

    private StoreController storeController;

    public StoreFacade() {
        this.storeController = StoreController.getInstance();
    }

    public void addNewStore(User owner, String storeName) {
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

    public Response<Collection<Store>> getAllStores() {
        return new Response<>(storeController.getAllStores());
    }

    public Response<Store> getStore(int id) {
        return new Response<>(storeController.getStore(id));
    }

    public Response<Set<Item>> getItemsWithNameContains(String name) {
        return new Response<>(storeController.getItemsWithNameContains(name));
    }

    public Response<Set<Item>> getItemsWithKeyWord(String kw) {
        return new Response<>(storeController.getItemsWithNameContains(kw));
    }

    public Response<Set<Item>> getItemsWithCategory(String category) {
        try {
            return new Response<>(storeController.getItemsWithNameContains(category));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Set<Item>> searchProducts(String productName, String category, List<String> keywords) {
        try {
            Set<Item> items = new HashSet<>();
            if (!productName.isEmpty())
                items = storeController.getItemsWithNameContains(productName);
            if (!category.isEmpty())
                items = storeController.getItemsWithNameContains(category).stream().filter(items::contains).collect(Collectors.toSet());
            if (!keywords.isEmpty())
                items = storeController.getItemsWithKeyWord(keywords).stream().filter(items::contains).collect(Collectors.toSet());
            if (!items.isEmpty())
                return new Response<>(items);
            throw new Exception("No items mach the search");
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}