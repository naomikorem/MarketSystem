package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Real extends Bridge {
    private SystemInterface adaptee;

    public Real() {
        this.adaptee = new SystemImplementor();
    }

    @Override
    public Response<Boolean> initializeMarket() {
        return this.adaptee.initializeMarket();
    }

    @Override
    public Response<Boolean> addExternalPurchaseService(String name) {
        return this.adaptee.addExternalPurchaseService(name);
    }

    @Override
    public Response<Boolean> addExternalSupplyService(String name) {
        return this.adaptee.addExternalSupplyService(name);
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
    public Response<User> register(String email, String name, String password) {
        return adaptee.register(email, name, password);
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
    public Response<Boolean> logout() {
        return adaptee.logout();
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
    public Response<List<Item>> getShoppingCartItems() {
        return adaptee.getShoppingCartItems();
    }

    @Override
    public Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount) {
        return null;
    }

    @Override
    public Response<Store> addNewStore(String name) {
        return adaptee.addNewStore(name);
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
    public Response<Boolean> addOwner(String owner, int storeId) {
        return adaptee.addOwner(owner, storeId);
    }

    @Override
    public Response<Boolean> removeOwner(String toRemove, int storeId) {
        return adaptee.removeOwner(toRemove, storeId);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount) {
        return adaptee.addItemToStore(storeId, name, category, price, amount);
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
    public Response<Boolean> updateStorePolicy(int storeId) {
        return null;
    }

    @Override
    public Response<Boolean> updateDiscountPolicy(int storeId) {
        return null;
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        return adaptee.addItemToCart(storeId, itemId, amount);
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        return adaptee.getCartBaskets();
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        return adaptee.removeItemFromStore(storeId, itemId, amount);
    }

    @Override
    public Response<Map<Item, Integer>> getItems(int storeId) {
        return adaptee.getItems(storeId);
    }
}
