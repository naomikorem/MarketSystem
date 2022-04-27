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
    public Response<Boolean> initializeMarket() {
        if (this.real != null) {
            return real.initializeMarket();
        }
        return null;
    }

    @Override
    public Response<Boolean> addExternalPurchaseService(String name) {
        if (this.real != null) {
            return real.addExternalPurchaseService(name);
        }
        return null;
    }

    @Override
    public Response<Boolean> addExternalSupplyService(String name) {
        if (this.real != null) {
            return real.addExternalSupplyService(name);
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
    public Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name) {
        if (this.real != null) {
            return real.purchaseShoppingCart(username, address, purchase_service_name, supply_service_name);
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
    public Response<Boolean> logout() {
        if (this.real != null) {
            return real.logout();
        }
        return null;
    }

    @Override
    public Response<Collection<Store>> getStores() {

        return real.getStores();
    }

    @Override
    public Response<Store> getStoreInformation(int storeID) {

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

    @Override
    public Response<List<Item>> getShoppingCartItems() {

        return real.getShoppingCartItems();
    }

    @Override
    public Response<Store> addNewStore(String storeName) {
        return real.addNewStore(storeName);
    }
}
