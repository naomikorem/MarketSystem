package DomainLayer;

import DomainLayer.Stores.*;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Predicates.CompositePredicate;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.*;
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

    public Response<Collection<Store>> getAllOpenStores() {
        return new Response<>(storeController.getAllOpenStores());
    }

    public Response<Collection<Store>> getStoresBesidesPermanentlyClosed() {
        return new Response<>(storeController.getStoresBesidesPermanentlyClosed());
    }

    public Response<Store> getStore(int id) {
        try {
            Store s = storeController.getStore(id);
            if (s == null) {
                throw new IllegalArgumentException(String.format("Could not find store with id %s", id));
            }
            return new Response<>(s);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
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

    public Response<Double> getRatingOfProduct(int storeId, int itemId) {
        try {
            return new Response<>(storeController.getRatingOfItem(storeId, itemId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> setRatingOfProduct(int storeId, int itemId, double rate) {
        try {
            return new Response<>(storeController.setRatingOfItem(storeId, itemId, rate));
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

    public Response<Item> addItemToStore(User manager, int storeId, String name, Category category, double price, int amount) {
        try {
            return new Response<>(storeController.addItemToStore(manager, storeId, name, category, price, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> addItemToStore(User manager, int storeId, String name, String category, double price, int amount) {
        try {
            Category c = Category.valueOf(category);
            return new Response<>(storeController.addItemToStore(manager, storeId, name, c, price, amount));
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

    public Response<Store> closeStore(User founder, int storeId) {
        try {
            return new Response<>(storeController.closeStore(founder, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Store> reopenStore(User founder, int storeId) {
        try {
            return new Response<>(storeController.reopenStore(founder, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Store> permanentlyCloseStore(int storeId) {
        try {
            return new Response<>(storeController.permanentlyCloseStore(storeId));
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
  
    public Response<List<String>> getManagers(User owner, int storeId){
        try {
            return new Response<>(storeController.getManagers(owner, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Permission> getManagersPermissions(User owner, int storeId, String managerName){
        try {
            return new Response<>(storeController.getManagersPermissions(owner, storeId, managerName));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> modifyItem(User owner, int storeId, int itemId, String productName, String category, double price, int amount, List<String> keywords) {
        try {
            return new Response<>(storeController.modifyItem(owner, storeId, itemId, productName, category, price, amount, keywords));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Item> setItemAmount(User owner, int storeId, int itemId, int amount) {
        try {
            return new Response<>(storeController.setItemAmount(owner, storeId, itemId, amount));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Map<Item, Integer>> getItems(int storeId) {
        try {
            return new Response<>(storeController.getItems(storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
  
    public Response<Boolean> isOwner(int store_id, String username) {
        try {
            return new Response<>(storeController.getStore(store_id).isOwner(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> applyChangeName(User u, String oldName, String newName) {
        try {
            storeController.applyChangeName(u, oldName, newName);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<SimpleDiscountPolicy> addDiscount(User u, int storeId, double percentage) {
        try {
            return new Response<>(storeController.addDiscount(u, storeId, percentage));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<AbstractPurchasePolicy> addPolicy(User u, int storeId) {
        try {
            return new Response<>(storeController.addPolicy(u, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeDiscount(User u, int storeId, int discountId) {
        try {
            storeController.removeDiscount(u, storeId, discountId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removePolicy(User u, int storeId, int policyId) {
        try {
            storeController.removePolicy(u, storeId, policyId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<SimpleDiscountPolicy> addExclusiveDiscount(User u, int storeId, double percentage) {
        try {
            return new Response<>(storeController.addExclusiveDiscount(u, storeId, percentage));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addItemPredicateToDiscount(User owner, int storeId, int discountId, String type, int itemId) {
        try {
            storeController.addItemPredicateToDiscount(owner, storeId, discountId, type, itemId);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<SimpleDiscountPolicy>> getAllDiscountPolicies(User owner, int storeId) {
        try {
            return new Response<>(storeController.getAllDiscountPolicies(owner, storeId));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
    public Response<Boolean> addItemPredicateToPolicy(User owner, int storeId, int policyId, String type, int itemId, int hour) {
        try {
            storeController.addItemPredicateToPolicy(owner, storeId, policyId, type, itemId,hour);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addItemNotAllowedInDatePredicateToPolicy(User owner, int storeId, int policyId, String type, int itemId, Calendar date) {
        try {
            storeController.addItemNotAllowedInDatePredicateToPolicy(owner, storeId, policyId, type, itemId,date);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<Boolean> addCategoryPredicateToDiscount(User owner, int storeId, int discountId, String type, String categoryName) {
        try {
            storeController.addCategoryPredicateToDiscount(owner, storeId, discountId, type, categoryName);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addBasketRequirementPredicateToDiscount(User owner, int storeId, int discountId, String type, double minPrice) {
        try {
            storeController.addBasketRequirementPredicateToDiscount(owner, storeId, discountId, type, minPrice);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Double> getShoppingBasketPrice(ShoppingBasket sb) {
        try {
            return new Response<>(storeController.getShoppingBasketPrice(sb));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> getShoppingBasketPurchesPolicy(ShoppingBasket sb) {
        try {
            return new Response<>(storeController.getShoppingBasketPolicy(sb));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<String> getStoreNameByID(int id) {
        try {
            return new Response<>(storeController.getStoreNameByID(id));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


}