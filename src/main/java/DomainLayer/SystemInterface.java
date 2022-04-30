package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.HistoryManagement.History;
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

    public Response<Boolean> initializeMarket();

    public Response<Boolean> addExternalPurchaseService(String name);

    public Response<Boolean> addExternalSupplyService(String name);

    public Response<Boolean> removeExternalPurchaseService(String name);

    public Response<Boolean> removeExternalSupplyService(String name);

    public Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name);

    public Response<Boolean> hasPurchaseService();

    public Response<Boolean> hasSupplyService();

    public Response<Boolean> hasPurchaseService(String purchase_service_name);

    public Response<Boolean> hasSupplyService(String purchase_supply_name);

    public Response<History> getPurchaseHistory();
    public Response<History> getPurchaseHistory(String username);

    public Response<History> getStoreHistory(int store_id);

    public Response<Boolean> enter();

    public Response<Boolean> exit();

    public Response<User> register(String email, String name, String password);

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

    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount);

    Response<Boolean> deleteUser(String name);
    Response<List<String>> getStoreManagers(int storeId);

    Response<Permission> getManagersPermissions(int storeId, String managerName);

    Response<Boolean> setUserName(String newUserName);

    Response<Set<Item>> searchProducts(String productName, String category, List<String> keywords) ;
    Response<Set<Item>> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating);
}
