package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

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
    public Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name) {
        return this.adaptee.purchaseShoppingCart(username, address, purchase_service_name, supply_service_name);
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
    public Response<Boolean> logout() {
        return adaptee.logout();
    }

    @Override
    public Response<Collection<Store>>  getStores() {
        return adaptee.getAllStores();
    }

    @Override
    public Response<Store> getStoreInformation(int storeID) {
        try {
            return adaptee.getStore(storeID);
        }
        catch (Exception e){
            return new  Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Collection<Item>> searchProducts(String productName, String Category, List<String> keywords) {

        return null;
    }

    @Override
    public Response<Collection<Item>> filterResults() {

        return null;
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        return adaptee.getShoppingCartItems();
    }
    @Override
    public Response<Store> addNewStore(String name) {
        return adaptee.addNewStore(name);
    }
}
