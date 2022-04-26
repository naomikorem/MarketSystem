package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemImplementor implements SystemInterface {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private MarketManagementFacade marketManagementFacade;
    private User user;

    //Add catch to every function here.

    public SystemImplementor() {
        this.userFacade = new UserFacade();
        this.storeFacade = new StoreFacade();
        this.marketManagementFacade = MarketManagementFacade.getInstance();
    }

    @Override
    public Response<Boolean> enter() {
        try {
            if (this.user != null) {
                return new Response<>(false);
            }
            this.user = new User(new GuestState());
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Boolean> exit() {
        try {
            if (this.user != null && this.user.isLoggedIn()) {
                logout();
            } else {
                clearShoppingCart();
            }
            this.user = null;
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<User> register(String email, String name, String password) {
        return userFacade.register(email, name, password);
    }

    @Override
    public Response<User> login(String name, String password) {
        Response<User> r = userFacade.login(name, password);
        if (!r.hadError()) {
            this.user.setState(r.getObject().getState());
        }
        return r;
    }

    @Override
    public Response<Boolean> logout() {
        if (this.user == null || !this.user.isLoggedIn()) {
            return new Response<>("You have to be logged in to perform this action.");
        }
        clearShoppingCart();
        Response<Boolean> res = userFacade.logout(user.getName());
        if (res.getObject()) {
            this.user = new User(new GuestState());
        }
        return res;
    }

    private void clearShoppingCart() {
        for (ShoppingBasket basket : user.getCartBaskets()) {
            for (Map.Entry<Item, Integer> itemAmount : basket.getItemsAndAmounts()) {
                storeFacade.returnItemToStore(basket.getStoreId(), itemAmount.getKey(), itemAmount.getValue());
            }
        }
    }

    @Override
    public Response<Boolean> addManager(User owner, String manager, int storeId) {
        try {
            if (userFacade.isExist(manager)) {
                throw new IllegalArgumentException(String.format("There is no user by the name of %s", manager));
            }
            return storeFacade.addManager(owner, manager, storeId);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Collection<Store>> getAllStores() {
        return storeFacade.getAllStores();
    }

    @Override
    public Response<Store> getStore(int id) {
        return storeFacade.getStore(id);
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        try {
            return new Response<>(user.getShoppingCartItems());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        try {
            return new Response<>(user.getCartBaskets());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        try {
            Response<Item> itemRes = storeFacade.getAndDeductItemFromStore(storeId, itemId, amount);
            if (itemRes.hadError()) {
                return itemRes;
            }
            user.addItemToShoppingCart(storeId, itemRes.getObject(), amount);
            return itemRes;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Store> addNewStore(String name) {
        return storeFacade.addNewStore(user, name);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount) {
        return storeFacade.addItemToStore(user, storeId, name, category, price, amount);
    }

    public Response<Boolean> initializeMarket() {
        // check if there is system manager
        if (!userFacade.hasAdmin()) {
            // create the first system admin if there is no system manager
            Response<Boolean> res = userFacade.addAdmin("stub", "stub", "stub");
            if (res.hadError()) {
                return res;
            }
        }
        return this.marketManagementFacade.initializeMarket();
    }

    public Response<Boolean> addExternalPurchaseService(String name) {
        return this.marketManagementFacade.addExternalPurchaseService(name);
    }

    public Response<Boolean> addExternalSupplyService(String name) {
        return this.marketManagementFacade.addExternalSupplyService(name);
    }

    public Response<Boolean> removeExternalPurchaseService(String name) {
        return this.marketManagementFacade.removeExternalPurchaseService(name);
    }

    public Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name) {
        Response<User> user_res = userFacade.getUser(username);
        if (user_res.hadError())
            return new Response<>(user_res.getErrorMessage());

        User buying_user = user_res.getObject();
        /*List<Integer> stores_ids = buying_user.getCartBaskets().stream().map(ShoppingBasket::getStoreId).collect(Collectors.toList());
        List<Response<Store>> stores_response = stores_ids.stream().map(id -> storeFacade.getStore(id)).collect(Collectors.toList());

        List<Response<Store>> problematic_stores = stores_response.stream().filter(Response::hadError).collect(Collectors.toList());
        if (!problematic_stores.isEmpty())
        {
            StringBuilder error_msg = new StringBuilder();
            for (Response<Store> res : problematic_stores)
            {
                error_msg.append(res.getErrorMessage());
            }
            return new Response<>(error_msg.toString());
        }

        Map<Integer, Store> stores = stores_response.stream().map(Response::getObject).collect(Collectors.toConcurrentMap(Store::getStoreId, store -> store));
        */
        return this.marketManagementFacade.purchaseShoppingCart(buying_user, address, purchase_service_name, supply_service_name);
    }

    public Response<Boolean> hasPurchaseService() {
        return this.marketManagementFacade.hasPurchaseService();
    }

    public Response<Boolean> hasSupplyService() {
        return this.marketManagementFacade.hasSupplyService();
    }
}