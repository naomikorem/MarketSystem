package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.ShoppingBasket;
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
    public Response<Collection<Item>> searchProducts(String productName, String Category, List<String> keywords) {

        return null;
    }

    @Override
    public Response<Collection<Item>> filterResults() {

        return null;
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        if (this.real == null) {
            return null;
        }
        return real.getShoppingCartItems();
    }

    @Override
    public Response<Store> addNewStore(String storeName) {
        if (this.real == null) {
            return null;
        }
        return real.addNewStore(storeName);
    }

    @Override
    public Response<Boolean> addManager(String manager, int storeId) {
        return real.addManager(manager, storeId);
    }

    @Override
    public Response<Boolean> addOwner(String owner, int storeId) {
        return real.addOwner(owner, storeId);
    }

    @Override
    public Response<Boolean> removeOwner(String toRemove, int storeId) {
        return real.removeOwner(toRemove, storeId);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.addItemToStore(storeId, name, category, price, amount);
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.addItemToCart(storeId, itemId, amount);
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        if (this.real == null) {
            return null;
        }
        return real.getCartBaskets();
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        if (this.real == null) {
            return null;
        }
        return real.removeItemFromStore(storeId, itemId, amount);
    }
}
