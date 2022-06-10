package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import DomainLayer.SystemManagement.HistoryManagement.History;

import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.*;

public class Real extends Bridge {
    private SystemInterface adaptee;

    public Real() {
        this.adaptee = new SystemImplementor();
    }

    @Override
    public Response<Boolean> addExternalPurchaseService(String name, String url) {
        return this.adaptee.addExternalPurchaseService(name, url);
    }

    @Override
    public Response<Boolean> addExternalSupplyService(String name, String url) {
        return this.adaptee.addExternalSupplyService(name, url);
    }

    @Override
    public Response<Boolean> removeExternalPurchaseService(String name) {
        return this.adaptee.removeExternalPurchaseService(name);
    }

    @Override
    public Response<Boolean> removeExternalSupplyService(String name) {
        return this.adaptee.removeExternalSupplyService(name);
    }

    @Override
    public Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name) {
        return this.adaptee.purchaseShoppingCart(address, purchase_service_name, supply_service_name);
    }

    @Override
    public Response<Boolean> hasPurchaseService() {
        return this.adaptee.hasPurchaseService();
    }

    @Override
    public Response<Boolean> hasSupplyService() {
        return this.adaptee.hasSupplyService();
    }

    @Override
    public Response<Boolean> hasPurchaseService(String purchase_service_name) {
        return this.adaptee.hasPurchaseService(purchase_service_name);
    }

    public Response<Boolean> hasSupplyService(String supply_service_name) {
        return this.adaptee.hasSupplyService(supply_service_name);
    }

    @Override
    public Response<Boolean> enter() {
        return adaptee.enter();
    }

    @Override
    public Response<Boolean> exit() {
        return adaptee.exit();
    }

    @Override
    public Response<User> register(String email, String userName, String firstName, String lastName, String password) {
        return adaptee.register(email, userName, firstName, lastName, password);
    }

    @Override
    public Response<User> login(String name, String password) {
        return adaptee.login(name, password);
    }

    @Override
    public Response<Boolean> setUserName(String userName) {
        return adaptee.setUserName(userName);
    }

    @Override
    public Response<Collection<Store>> getStores() {
        return adaptee.getAllStores();
    }

    @Override
    public Response<Store> getStoreInformation(int storeID) {
        try {
            return adaptee.getStore(storeID);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Map<Item, Integer>> getItems(int storeId) {
        return adaptee.getItems(storeId);
    }

    @Override
    public Response<Permission> getManagersPermissions(int storeId, String managerName) {
        return adaptee.getManagersPermissions(storeId, managerName);
    }

    @Override
    public Response<Set<Item>> searchProducts(String productName, String Category, List<String> keywords) {

        return adaptee.searchProducts(productName, Category, keywords);
    }

    @Override
    public Response<Set<Item>> filterResults(Set<Item> items, int upLimit, int lowLimit, int rating) {

        return adaptee.filterProdacts(items, upLimit, lowLimit, rating);
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        return adaptee.addItemToCart(storeId, itemId, amount);
    }

    @Override
    public Response<Boolean> removeItemFromCart(int storeId, Item item, int amount) {
        return adaptee.removeItemFromCart(storeId, item, amount);
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        return adaptee.getShoppingCartItems();
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        return adaptee.getCartBaskets();
    }

    @Override
    public Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount) {
        return null;
    }

    @Override
    public Response<Boolean> logout() {
        return adaptee.logout();
    }

    @Override
    public Response<Store> addNewStore(String name) {
        return adaptee.addNewStore(name);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount) {
        return adaptee.addItemToStore(storeId, name, category, price, amount);
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
        return adaptee.addOwner(owner, storeId);
    }

    @Override
    public Response<Boolean> addManager(String manager, int storeId) {
        return adaptee.addManager(manager, storeId);
    }

    @Override
    public Response<Boolean> updateManagerPermissions(int storeId, String managerName, Byte newPermission) {
        return adaptee.setManagerPermission(managerName, storeId, newPermission);
    }

    @Override
    public Response<List<String>> getStoreManagers(int store) {
        return adaptee.getStoreManagers(store);
    }

    @Override
    public Response<Boolean> removeOwner(String toRemove, int storeId) {
        return adaptee.removeOwner(toRemove, storeId);
    }

    @Override
    public Response<Boolean> closeStore(int storeId) {
        return this.adaptee.closeStore(storeId);
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        return adaptee.removeItemFromStore(storeId, itemId, amount);
    }

    @Override
    public Response<History> getPurchaseHistory() {
        return adaptee.getPurchaseHistory();
    }

    @Override
    public Response<History> getPurchaseHistory(String username) {
        return adaptee.getPurchaseHistory(username);
    }

    @Override
    public Response<History> getStoreHistory(int store_id) {
        return adaptee.getStoreHistory(store_id);
    }

    @Override
    public Response<List<INotification>> getUserNotifications() {
        return this.adaptee.getUserNotifications();
    }

    @Override
    public Response<User> getUser(String userName) {
        return this.adaptee.getUser(userName);
    }

    @Override
    public Response<Boolean> removeManager(String toRemove, int storeId) {
        return this.adaptee.removeManager(toRemove, storeId);
    }

    @Override
    public Response<SimpleDiscountPolicy> addDiscount(int storeId, double percentage) {
        return this.adaptee.addDiscount(storeId, percentage);
    }

    @Override
    public Response<SimplePurchasePolicy> addPolicy(int storeId, int hour, Calendar date) {
        return this.adaptee.addPolicy(storeId, hour, date);
    }

    @Override
    public Response<Boolean> removePolicy(int storeId, int policyId) {
        return this.adaptee.removePolicy(storeId, policyId);
    }

    @Override
    public Response<AbstractPurchasePolicy> addItemPredicateToPolicy(int storeId, int policyId, String type, int itemId, int hour) {
        return this.adaptee.addItemPredicateToPolicy(storeId, policyId, type,itemId,hour);
    }

    public Response<AbstractPurchasePolicy> addItemNotAllowedInDatePredicateToPolicy(int storeId, int policyId, String type, int itemId, Calendar date) {
        return this.adaptee.addItemNotAllowedInDatePredicateToPolicy(storeId, policyId, type, itemId, date);
    }

    @Override
    public Response<Boolean> addItemPredicateToDiscount(int storeId, int discountId, String type, int itemId) {
        Response<AbstractDiscountPolicy> r = this.adaptee.addItemPredicateToDiscount(storeId, discountId, type, itemId);
        return r.hadError() ? new Response<>(true) : new Response<>(r.getErrorMessage());
    }

    @Override
    public Response<Boolean> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName) {
        Response<AbstractDiscountPolicy> r = this.adaptee.addCategoryPredicateToDiscount(storeId, discountId, type, categoryName);
        return r.hadError() ? new Response<>(true) : new Response<>(r.getErrorMessage());
    }

    @Override
    public Response<Double> getCartPrice() {
        return this.adaptee.getCartPrice();
    }

    public Response<Boolean> getIsLegalToPurchase(int storeId) {
        return adaptee.getIsLegalToPurchase(storeId);
    }

    @Override
    public Response<Boolean> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice) {
        Response<AbstractDiscountPolicy> r = this.adaptee.addBasketRequirementPredicateToDiscount(storeId, discountId, type, minPrice);
        return r.hadError() ? new Response<>(true) : new Response<>(r.getErrorMessage());
    }

    @Override
    public Response<SimpleDiscountPolicy> addExclusiveDiscount(int storeId, double percentage) {
        return this.adaptee.addExclusiveDiscount(storeId, percentage);
    }

    @Override
    public Response<Boolean> removeDiscount(int storeId, int discountId) {
        return this.adaptee.removeDiscount(storeId, discountId);
    }

    @Override
    public Response<Boolean> permanentlyCloseStore(int storeId) {
        return this.adaptee.permanentlyCloseStore(storeId);
    }

    @Override
    public Response<List<String>> getStoreOwners(int store_id) {
        return this.adaptee.getStoreOwners(store_id);
    }

    @Override
    public Response<Boolean> hasAdmin() {
        return adaptee.hasAdmin();
    }

    @Override
    public Response<Boolean> addAdmin(String name) {
        return adaptee.addAdmin(name);
    }

    @Override
    public Response<Boolean> deleteAdmin(String name) {
        return adaptee.deleteAdmin(name);
    }

    public Response<Boolean> setItemRating(int storeId, int itemId, double rate) {return adaptee.setItemRating(storeId, itemId, rate); }

    public Response<Double> getItemRating(int storeId, int itemId) {return adaptee.getItemRating(storeId, itemId); }
}
