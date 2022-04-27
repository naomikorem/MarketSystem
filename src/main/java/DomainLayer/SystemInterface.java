package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

public interface SystemInterface {
    public Response<Boolean> initializeMarket();

    public Response<Boolean> addExternalPurchaseService(String name);

    public Response<Boolean> addExternalSupplyService(String name);

    public Response<Boolean> removeExternalPurchaseService(String name);

    public Response<Boolean> removeExternalSupplyService(String name);

    public Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name);

    public Response<Boolean> hasPurchaseService();

    public Response<Boolean> hasSupplyService();

    public Response<Boolean> hasPurchaseService(String purchase_service_name);

    public Response<Boolean> hasSupplyService(String purchase_supply_name);

    public Response<Boolean> enter();

    public Response<Boolean> exit();

    public Response<User> register(String email, String name, String password);

    public Response<User> login(String name, String password);

    public Response<Boolean> logout();

    public Response<List<Item>> getShoppingCartItems();

    public Response<Boolean> addManager(User owner, String manager, int storeId);

    public Response<Collection<Store>> getAllStores();

    public Response<Store> getStore(int id);

    Response<List<ShoppingBasket>> getCartBaskets();

    public Response<Item> addItemToCart(int storeId, int itemId, int amount);

    public Response<Store> addNewStore(String name);

    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount);
}
