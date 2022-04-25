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
    public Response<Store> getStoreInformation(String storeID) {
        try {
            return adaptee.getStore(Integer.parseInt(storeID));
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
}
