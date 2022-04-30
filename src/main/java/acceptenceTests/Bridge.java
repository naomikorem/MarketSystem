package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Bridge {
    //TODO: change return tipes.
    //Acceptance Tests for use case 1 System:
    public abstract Response<Boolean> initializeMarket();
    public abstract Response<Boolean> addExternalPurchaseService(String name);
    public abstract Response<Boolean> addExternalSupplyService(String name);
    public abstract Response<Boolean> removeExternalPurchaseService(String name);
    public abstract Response<Boolean> removeExternalSupplyService(String name);
    public abstract Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name);
    public abstract Response<Boolean> hasPurchaseService();
    public abstract Response<Boolean> hasSupplyService();
    public abstract Response<Boolean> hasPurchaseService(String purchase_service_name);
    public abstract Response<Boolean> hasSupplyService(String supply_service_name);

    //Acceptance Tests for use case 1 Users:
    public abstract Response<Boolean> enter();
    public abstract Response<Boolean> exit();
    public abstract Response<User> register(String email, String name, String password);
    public abstract Response<User> login(String user, String password);

    public abstract Response<Boolean> setUserName(String userName);


    //Acceptance Tests for use case 2:
    public abstract Response<Collection<Store>> getStores();
    public abstract Response<Store> getStoreInformation(int storeID);

    public abstract Response<Permission> getManagersPermissions(int storeId, String managerName);

    public abstract Response<Set<Item>> searchProducts(String productName, String Category, List<String> keywords);
    public abstract Response<Set<Item>> filterResults(Set<Item> items, int upLimit, int lowLimit, int rating);

    public abstract Response<Item> addItemToCart(int storeId, int itemId, int amount);
    public abstract Response<List<Item>> getShoppingCartItems();
    public abstract Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount);

    //Acceptance Tests for use case 3:
    public abstract Response<Boolean> logout();
    public abstract Response<Store> addNewStore(String storeName);

    //Acceptance Tests for use case 4:
    public abstract Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount);

    public abstract Response<History> getPurchaseHistory();
    public abstract Response<History> getPurchaseHistory(String username);
    public abstract Response<History> getStoreHistory(int store_id);
    public abstract Response<Boolean> updateStorePolicy(int storeId);
    public abstract Response<Boolean> updateDiscountPolicy(int storeId);
    public abstract Response<Boolean> addOwner(String owner, int storeId);
    public abstract Response<Boolean> addManager(String managerName, int storeId);
    public abstract Response<Boolean> updateManagerPermissions(int storeId, String managerName, Byte newPermission);
    public abstract Response<List<String>> getStoreManagers(int store);
    public abstract Response<Boolean> removeOwner(String toRemove, int storeId);
    public abstract Response<Boolean> closeStore(int storeId);
    public abstract Response<List<INotification>> getUserNotifications();
    public abstract Response<Boolean> removeManager(String toRemove, int storeId);
    public abstract Response<Boolean> permanentlyCloseStore(int storeId);



    public abstract Response<Item> removeItemFromStore(int storeId, int itemId, int amount);
    public abstract Response<Map<Item, Integer>> getItems(int storeId);



    //Acceptance Tests for use case 5:
    //Acceptance Tests for use case 6:
    public abstract Response<Boolean> addAdmin(String name);
    public abstract Response<Boolean> deleteAdmin(String name);


    public abstract Response<List<ShoppingBasket>> getCartBaskets();
}
