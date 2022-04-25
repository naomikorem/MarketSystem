package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

public class Proxy extends Bridge {
    private Bridge real;

    public Proxy(Bridge bridge) {
        this.real = bridge;
    }

    @Override
    public void enter() {
        if (this.real != null) {
            real.enter();
        }
    }

    @Override
    public void exit() {
        if (this.real != null) {
            real.exit();
        }
    }

    @Override
    public Response<User> register(String email, String name, String password) {
        if (this.real != null) {
            return real.register(email, name, password);
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
    public Response<Collection<Store>> getStores() {

        return real.getStores();
    }

    @Override
    public Response<Store> getStoreInformation(String storeID) {

        return real.getStoreInformation(storeID);
    }

    @Override
    public Response<Collection<Item>> searchProducts(String productName, String Category, List<String> keywords) {

        return null;
    }

    @Override
    public Response<Collection<Item>> filterResults() {

        return null;
    }
}