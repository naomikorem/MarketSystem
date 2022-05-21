package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.*;

public class Proxy extends Bridge {
    private Bridge real;

    public Proxy(Bridge bridge) {
        this.real = bridge;
    }

    @Override
    public Response<Boolean> addExternalPurchaseService(String name, String url) {
        if (this.real != null) {
            return real.addExternalPurchaseService(name, url);
        }
        return null;
    }

    @Override
    public Response<Boolean> addExternalSupplyService(String name, String url) {
        if (this.real != null) {
            return real.addExternalSupplyService(name, url);
        }
        return null;
    }

    @Override
    public Response<Boolean> removeExternalPurchaseService(String name) {
        if (this.real != null) {
            return real.removeExternalPurchaseService(name);
        }
        return null;
    }

    @Override
    public Response<Boolean> removeExternalSupplyService(String name) {
        if (this.real != null) {
            return real.removeExternalSupplyService(name);
        }
        return null;
    }

    @Override
    public Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name) {
        if (this.real != null) {
            return real.purchaseShoppingCart(address, purchase_service_name, supply_service_name);

        }
        return null;
    }

    @Override
    public Response<Boolean> hasPurchaseService() {
        if (this.real != null) {
            return real.hasPurchaseService();
        }
        return null;
    }

    @Override
    public Response<Boolean> hasSupplyService() {
        if (this.real != null) {
            return real.hasSupplyService();
        }
        return null;
    }

    @Override
    public Response<Boolean> hasPurchaseService(String purchase_service_name) {
        if (this.real != null) {
            return real.hasPurchaseService(purchase_service_name);
        }
        return null;
    }

    @Override
    public Response<Boolean> hasSupplyService(String supply_service_name) {
        if (this.real != null) {
            return real.hasSupplyService(supply_service_name);
        }
        return null;
    }

    @Override
    public Response<Boolean> enter() {
        if (this.real != null) {
            return real.enter();
        }
        return null;
    }

    @Override
    public Response<Boolean> exit() {
        if (this.real != null) {
            return real.exit();
        }
        return null;
    }

    @Override
    public Response<User> register(String email, String userName, String firstName, String lastName, String password) {
        if (this.real != null) {
            return real.register(email, userName, firstName, lastName, password);
        }
        return null;
    }

    @Override
    public Response<User> login(String email, String password) {
        if (this.real != null) {
            return real.login(email, password);
        }
        return null;
    }

    @Override
    public Response<Boolean> setUserName(String userName) {
        if (this.real != null) {
            return real.setUserName(userName);
        }
        return null;
    }

    @Override
    public Response<Collection<Store>> getStores() {
        if (this.real == null) {
            return null;
        }
        return real.getStores();
    }

    @Override
    public Response<Store> getStoreInformation(int storeID) {
        if (this.real == null) {
            return null;
        }
        return real.getStoreInformation(storeID);
    }

    @Override
    public Response<Map<Item, Integer>> getItems(int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.getItems(storeId);
    }

    @Override
    public Response<Permission> getManagersPermissions(int storeId, String managerName) {
        if (this.real == null) {
            return null;
        }
        return real.getManagersPermissions(storeId, managerName);
    }

    @Override
    public Response<Set<Item>> searchProducts(String productName, String Category, List<String> keywords) {


        if (this.real == null)
            return null;
        return real.searchProducts(productName,Category, keywords);
    }

    @Override
    public Response<Set<Item>> filterResults(Set<Item> items, int upLimit, int lowLimit, int rating) {
        if (this.real == null)
            return null;
        return real.filterResults(items, upLimit, lowLimit, rating);
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.addItemToCart(storeId, itemId, amount);
    }

    @Override
    public Response<Boolean> removeItemFromCart(int storeId, Item item, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.removeItemFromCart(storeId, item, amount);
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        if (this.real == null) {
            return null;
        }
        return real.getShoppingCartItems();
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        if (this.real == null) {
            return null;
        }
        return real.getCartBaskets();
    }

    @Override
    public Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount) {
        return null;
    }

    @Override
    public Response<Boolean> logout() {
        if (this.real != null) {
            return real.logout();
        }
        return null;
    }

    @Override
    public Response<Store> addNewStore(String storeName) {
        if (this.real == null) {
            return null;
        }
        return real.addNewStore(storeName);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.addItemToStore(storeId, name, category, price, amount);
    }

    @Override
    public Response<Boolean> updateStorePolicy(int storeId) {
        return null;
    }

    @Override
    public Response<Boolean> updateDiscountPolicy(int storeId) {
        return null;
    }

    @Override
    public Response<Boolean> addOwner(String owner, int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.addOwner(owner, storeId);
    }

    @Override
    public Response<Boolean> addManager(String manager, int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.addManager(manager, storeId);
    }

    @Override
    public Response<Boolean> updateManagerPermissions(int storeId, String managerName, Byte newPermission) {
        if (this.real == null)
            return null;
        return real.updateManagerPermissions(storeId, managerName, newPermission);
    }

    @Override
    public Response<List<String>> getStoreManagers(int store) {
        if (this.real == null)
            return null;
        return real.getStoreManagers(store);
    }

    @Override
    public Response<Boolean> removeOwner(String toRemove, int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.removeOwner(toRemove, storeId);
    }

    @Override
    public Response<Boolean> closeStore(int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.closeStore(storeId);
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.removeItemFromStore(storeId, itemId, amount);
    }

    @Override
    public Response<History> getPurchaseHistory() {
        if (this.real == null) {
            return null;
        }
        return real.getPurchaseHistory();
    }

    @Override
    public Response<History> getPurchaseHistory(String username) {
        if (this.real == null) {
            return null;
        }
        return real.getPurchaseHistory(username);
    }

    @Override
    public Response<History> getStoreHistory(int store_id) {
        if (this.real == null) {
            return null;
        }
        return real.getStoreHistory(store_id);
    }

    @Override
    public Response<List<INotification>> getUserNotifications() {
        if (this.real == null) {
            return null;
        }
        return real.getUserNotifications();
    }

    @Override
    public Response<List<INotification>> getUserRealTimeNotifications() {
        if (this.real == null) {
            return null;
        }
        return real.getUserRealTimeNotifications();
    }

    @Override
    public Response<User> getUser(String userName) {
        if (this.real == null) {
            return null;
        }
        return real.getUser(userName);
    }

    @Override
    public Response<Boolean> removeManager(String toRemove, int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.removeManager(toRemove, storeId);
    }

    @Override
    public Response<AbstractDiscountPolicy> addDiscount(int storeId, double percentage) {
        if (this.real == null) {
            return null;
        }
        return real.addDiscount(storeId, percentage);
    }

    @Override
    public Response<AbstractPurchasePolicy> addPolicy (int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.addPolicy(storeId);
    }

    @Override
    public Response<Boolean> removePolicy(int storeId, int policyId) {
        if (this.real == null) {
            return null;
        }
        return real.removePolicy(storeId,policyId);
    }

    @Override
    public Response<Boolean> addItemPredicateToPolicy(int storeId, int policyId, String type, int itemId, int hour) {
        if (this.real == null) {
            return null;
        }
        return real.addItemPredicateToPolicy(storeId,policyId,type,itemId,hour);
    }

    @Override
    public Response<Boolean> addItemNotAllowedInDatePredicateToPolicy(int storeId, int policyId, String type, int itemId, Calendar date) {
        if (this.real == null) {
            return null;
        }
        return real.addItemNotAllowedInDatePredicateToPolicy(storeId,policyId,type,itemId,date);
    }

    @Override
    public Response<Boolean> addItemPredicateToDiscount(int storeId, int discountId, String type, int itemId) {
        if (this.real == null) {
            return null;
        }
        return real.addItemPredicateToDiscount(storeId, discountId, type, itemId);
    }

    @Override
    public Response<Boolean> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName) {
        if (this.real == null) {
            return null;
        }
        return real.addCategoryPredicateToDiscount(storeId, discountId, type, categoryName);
    }

    @Override
    public Response<Double> getCartPrice() {
        if (this.real == null) {
            return null;
        }
        return real.getCartPrice();
    }

    public Response<Boolean> getIsLegalToPurchase(int storeId) {
        if(this.real == null) {
            return null;
        }
        return real.getIsLegalToPurchase(storeId);
    }

    @Override
    public Response<Boolean> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice) {
        if (this.real == null) {
            return null;
        }
        return real.addBasketRequirementPredicateToDiscount(storeId, discountId, type, minPrice);
    }

    @Override
    public Response<AbstractDiscountPolicy> addExclusiveDiscount(int storeId, double percentage) {
        if (this.real == null) {
            return null;
        }
        return real.addExclusiveDiscount(storeId, percentage);
    }

    @Override
    public Response<Boolean> removeDiscount(int storeId, int discountId) {
        if (this.real == null) {
            return null;
        }
        return real.removeDiscount(storeId, discountId);
    }

    @Override
    public Response<Boolean> permanentlyCloseStore(int storeId) {
        if (this.real == null) {
            return null;
        }
        return real.permanentlyCloseStore(storeId);
    }

    @Override
    public Response<List<String>> getStoreOwners(int store_id) {
        if (this.real == null) {
            return null;
        }
        return real.getStoreOwners(store_id);
    }

    @Override
    public Response<Boolean> hasAdmin() {
        if (this.real == null) {
            return null;
        }
        return real.hasAdmin();
    }

    @Override
    public Response<Boolean> addAdmin(String name) {
        if (this.real == null) {
            return null;
        }
        return real.addAdmin(name);
    }

    @Override
    public Response<Boolean> deleteAdmin(String name) {
        if (this.real == null) {
            return null;
        }
        return real.deleteAdmin(name);
    }

    public Response<Boolean> setItemRating(int storeId, int itemId, double rate) {
        if (this.real == null) {
            return null;
        }
        return real.setItemRating(storeId, itemId, rate);
    }

    public Response<Double> getItemRating(int storeId, int itemId){
        if (this.real == null) {
            return null;
        }
        return real.getItemRating(storeId, itemId);
    }
}
