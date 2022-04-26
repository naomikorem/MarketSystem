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

    public Response<Store> addNewStore(User owner, String storeName) {
        try {
            if (!owner.isLoggedIn()) {
                throw new RuntimeException("The user is not logged in.");
            }
            return new Response<>(storeController.createStore(owner, storeName));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addManager(User owner, String manager, int storeId) {
        try {
            return new Response<>(storeController.addManager(owner, manager, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Collection<Store>> getAllStores() {
        return new Response<>(storeController.getAllStores());
    }

    public Response<Store> getStore(int id) {
        Store s = storeController.getStore(id);
        if (s == null) {
            throw new IllegalArgumentException(String.format("Could not find store with id %s", id));
        }
        return new Response<>(s);
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

    public Response<Item> getItemFromStore(int storeId, int itemId) {
        try {
            return new Response<>(storeController.getItemFromStore(storeId, itemId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> getAndDeductItemFromStore(int storeId, int itemId, int amount) {
        return new Response<>(storeController.getAndDeductItemFromStore(storeId, itemId, amount));
    }

    public Response<Item> addItemToStore(User manager, int storeId, String name, String category, double price, int amount) {
        try {
            return new Response<>(storeController.addItemToStore(manager, storeId, name, category, price, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> returnItemToStore(int storeId, Item item, int amount) {
        try {
            return new Response<>(storeController.returnItemToStore(storeId, item, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}