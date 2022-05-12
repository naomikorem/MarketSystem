package DomainLayer;

import DomainLayer.Stores.Category;
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

public interface SystemInterface {
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount);

    public Response<Item> modifyItem(int storeId, int itemId, String productName, String category, double price, List<String> keywords);

    public Response<Map<Item, Integer>> getItems(int storeId);


    public Response<List<String>> getStoreOwners(int storeId);


    public Response<Boolean> addExternalPurchaseService(String name, String url);

    public Response<Boolean> addExternalSupplyService(String name, String url);

    public Response<Boolean> removeExternalPurchaseService(String name);

    public Response<Boolean> removeExternalSupplyService(String name);


    public Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name);

    public Response<Boolean> hasPurchaseService();

    public Response<Boolean> hasSupplyService();

    public Response<Boolean> hasPurchaseService(String purchase_service_name);

    public Response<Boolean> hasSupplyService(String purchase_supply_name);


    public Response<History> getPurchaseHistory();
    public Response<History> getPurchaseHistory(String username);
    public Response<User> getUser(String userName);

    public Response<History> getStoreHistory(int store_id);

    public Response<Boolean> enter();

    public Response<Boolean> exit();

    public Response<User> register(String email, String userName, String firstName, String lastName, String password);

    public Response<User> login(String user, String password);

    public Response<Boolean> logout();

    public Response<List<Item>> getShoppingCartItems();

    public Response<Boolean> addManager(String manager, int storeId);

    Response<Boolean> addOwner(String owner, int storeId);

    Response<Boolean> setManagerPermission(String manager, int storeId, byte permission);

    public Response<Collection<Store>> getAllStores();

    public Response<Store> getStore(int id);

    Response<List<ShoppingBasket>> getCartBaskets();

    public Response<Item> addItemToCart(int storeId, int itemId, int amount);

    public Response<Boolean> closeStore(int storeId);

    public Response<Boolean> permanentlyCloseStore(int storeId);

    Response<Boolean> removeOwner(String toRemove, int storeId);

    Response<Boolean> removeManager(String toRemove, int storeId);

    public Response<Store> addNewStore(String name);

    public Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount);

    public Response<Boolean> deleteUser(String name);
    public Response<List<String>> getStoreManagers(int storeId);

    public Response<Permission> getManagersPermissions(int storeId, String managerName);

    public Response<Boolean> setUserName(String newUserName);

    public Response<List<INotification>> getUserNotifications();

    public Response<Set<Item>> searchProducts(String productName, String category, List<String> keywords) ;
    public Response<Set<Item>> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating);

    public Response<Boolean> deleteAdmin(String name);
    public Response<Boolean> addAdmin(String name);

    public Response<Boolean> hasAdmin();


    public Response<Boolean> removeItemFromCart(int storeId, Item item, int amount);

    public Response<List<INotification>> getUserRealTimeNotifications();
}
