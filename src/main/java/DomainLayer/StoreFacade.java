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
            if (!owner.isSubscribed()) {
                throw new RuntimeException("The user is not logged in.");
            }
            return new Response<>(storeController.createStore(owner, storeName));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addManager(User owner, User manager, int storeId) {
        try {
            return new Response<>(storeController.addManager(owner, manager, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addOwner(User owner, User newOwner, int storeId) {
        try {
            return new Response<>(storeController.addOwner(owner, newOwner, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> isExistStore(int storeId) {
        try {
            return new Response<>(storeController.isExist(storeId));
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
            if (!category.isEmpty()) {
                Set<Item> finalItems = items;
                items = storeController.getItemsWithCategory(category).stream().filter(i -> finalItems.contains(i) || finalItems.isEmpty()).collect(Collectors.toSet());
            }
            if (!keywords.isEmpty()) {
                Set<Item> finalItems1 = items;
                items = storeController.getItemsWithKeyWord(keywords).stream().filter(i -> finalItems1.contains(i) || finalItems1.isEmpty()).collect(Collectors.toSet());
            }
            if (!items.isEmpty())
                return new Response<>(items);
            throw new Exception("No items mach the search");
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /**
     * @param items serched items
     * @param upLimit if irrelevant value = -1
     * @param lowLimit if irrelevant value = -1
     * @param rating if irrelevant value = -1
     * @return filtered prodacts
     */
    public Response<Set<Item>> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating){
        return new Response<>(storeController.filterProdacts(items,upLimit,lowLimit, rating));
    }


    public Response<Item> getItemFromStore(int storeId, int itemId) {
        try {
            return new Response<>(storeController.getItemFromStore(storeId, itemId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> reserveItemFromStore(int storeId, int itemId, int amount) {
        try {
            return new Response<>(storeController.reserveItemFromStore(storeId, itemId, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> removeItemFromStore(User owner, int storeId, int itemId, int amount) {
        try {
            return new Response<>(storeController.removeItemFromStore(owner, storeId, itemId, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
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

    public Response<Boolean> setManagerPermission(User owner, String manager, int storeId, byte permission) {
        try {
            storeController.setManagerPermission(owner, manager, storeId, permission);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> closeStore(User founder, int storeId) {
        try {
            storeController.closeStore(founder, storeId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> permanentlyCloseStore(int storeId) {
        try {
            storeController.permanentlyCloseStore(storeId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeOwner(User owner, User toRemove, int storeId) {
        try {
            storeController.removeOwner(owner, toRemove, storeId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeManager(User owner, User toRemove, int storeId) {
        try {
            storeController.removeManager(owner, toRemove, storeId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeUserRoles(User owner, User toRemove) {
        try {
            storeController.removeUserRoles(owner, toRemove);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
    public Response<List<User>> getManagers(User owner, int storeId){
        try {
            return new Response<>(storeController.getManagers(owner, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> modifyItem(User owner, int storeId, int itemId, String productName, String category, double price, List<String> keywords) {
        try {
            return new Response<>(storeController.modifyItem(owner, storeId, itemId, productName, category, price, keywords));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}