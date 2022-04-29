package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

public abstract class Bridge {
    //TODO: change return tipes.
    //Acceptance Tests for use case 1 System:
    public abstract Response<Boolean> initializeMarket();
    public abstract Response<Boolean> addExternalPurchaseService(String name);
    public abstract Response<Boolean> addExternalSupplyService(String name);
    public abstract Response<Boolean> removeExternalPurchaseService(String name);
    public abstract Response<Boolean> removeExternalSupplyService(String name);
    public abstract Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name);
    public abstract Response<Boolean> hasPurchaseService();
    public abstract Response<Boolean> hasSupplyService();
    public abstract Response<Boolean> hasPurchaseService(String purchase_service_name);
    public abstract Response<Boolean> hasSupplyService(String supply_service_name);

    //Acceptance Tests for use case 1 Users:
    public abstract Response<Boolean> enter();
    public abstract Response<Boolean> exit();
    public abstract Response<User> register(String email, String name, String password);
    public abstract Response<User> login(String user, String password);


    //Acceptance Tests for use case 2:
    public abstract Response<Collection<Store>> getStores();
    public abstract Response<Store> getStoreInformation(int storeID);
    public abstract Response<Collection<Item>> searchProducts(String productName, String Category, List<String> keywords);
    public abstract Response<Collection<Item>> filterResults();
    public abstract Response<Item> addItemToCart(int storeId, int itemId, int amount);
    public abstract Response<List<Item>> getShoppingCartItems();
    public abstract Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount);

    //Acceptance Tests for use case 3:
    public abstract Response<Boolean> logout();
    public abstract Response<Store> addNewStore(String storeName);

    //Acceptance Tests for use case 4:
    public abstract Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount);

    public abstract Response<Boolean> updateStorePolicy(int storeId);
    public abstract Response<Boolean> updateDiscountPolicy(int storeId);
    public abstract Response<Boolean> addOwner(String owner, int storeId);
    public abstract Response<Boolean> addManager(String managerName, int storeId);
    public abstract Response<Boolean> updateManagerPermissions(int storeId, String managerName, Byte newPermission);
    public abstract Response<List<User>> getStoreManagers(int store);
    public abstract Response<Boolean> removeOwner(String toRemove, int storeId);




    public abstract Response<Item> removeItemFromStore(int storeId, int itemId, int amount);



    //Acceptance Tests for use case 5:
    //Acceptance Tests for use case 6:

    public abstract Response<List<ShoppingBasket>> getCartBaskets();
}
