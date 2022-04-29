package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.TODO;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemImplementor implements SystemInterface {
    @TODO
    //move checks if user == null to the delegating class when sockets are added

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
            if (this.user != null && this.user.isSubscribed()) {
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
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return userFacade.register(email, name, password);
    }

    @Override
    public Response<User> login(String name, String password) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if (user.isSubscribed()) {
            return new Response<>("You have to log out before attempting to log in to a user.");
        }
        Response<User> r = userFacade.login(name, password);
        if (!r.hadError()) {
            this.user.setState(r.getObject().getState());
        }
        return r;
    }

    @Override
    public Response<Boolean> logout() {
        if (this.user == null || !this.user.isSubscribed()) {
            return new Response<>("You have to be logged in to perform this action.");
        }
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
    public Response<Boolean> addManager(String managerName, int storeId) {
        try {
            if (!userFacade.isExist(managerName)) {
                throw new IllegalArgumentException(String.format("There is no user by the name of %s", managerName));
            }
            return storeFacade.addManager(user, userFacade.getUser(managerName).getObject(), storeId);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Boolean> addOwner(String owner, int storeId) {
        try {
            if (!userFacade.isExist(owner)) {
                throw new IllegalArgumentException(String.format("There is no user by the name of %s", owner));
            }
            return storeFacade.addOwner(user, userFacade.getUser(owner).getObject(), storeId);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Boolean> setManagerPermission(String manager, int storeId, byte permission) {
        try {
            return storeFacade.setManagerPermission(user, manager, storeId, permission);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Collection<Store>> getAllStores() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getAllStores();
    }

    @Override
    public Response<Store> getStore(int id) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getStore(id);
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        try {
            if (user == null) {
                return new Response<>("Enter the system properly in order to perform actions in it.");
            }
            return new Response<>(user.getShoppingCartItems());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<List<ShoppingBasket>> getCartBaskets() {
        try {
            if (user == null) {
                return new Response<>("Enter the system properly in order to perform actions in it.");
            }
            return new Response<>(user.getCartBaskets());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId, int amount) {
        try {
            if (user == null) {
                return new Response<>("Enter the system properly in order to perform actions in it.");
            }
            Response<Item> itemRes = storeFacade.reserveItemFromStore(storeId, itemId, amount);
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
    public Response<Boolean> closeStore(int storeId) {
        return storeFacade.closeStore(user, storeId);
    }

    @Override
    public Response<Boolean> permanentlyCloseStore(int storeId) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<Boolean> r1 = userFacade.isAdmin(user.getName());
        if (r1.hadError() || !r1.getObject()) {
            return r1;
        } else {
            return storeFacade.permanentlyCloseStore(storeId);
        }
    }

    @Override
    public Response<Boolean> removeOwner(String toRemove, int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<User> r = userFacade.getUser(toRemove);
        if (r.hadError()) {
            return new Response<>(r.getErrorMessage());
        }
        return storeFacade.removeOwner(user, r.getObject(), storeId);
    }

    @Override
    public Response<Boolean> removeManager(String toRemove, int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<User> r = userFacade.getUser(toRemove);
        if (r.hadError()) {
            return new Response<>(r.getErrorMessage());
        }
        return storeFacade.removeManager(user, r.getObject(), storeId);
    }

    @Override
    public Response<Store> addNewStore(String name) {
        return storeFacade.addNewStore(user, name);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount) {
        return storeFacade.addItemToStore(user, storeId, name, category, price, amount);
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        return storeFacade.removeItemFromStore(user, storeId, itemId, amount);
    }

    public Response<Boolean> initializeMarket() {
        return this.marketManagementFacade.initializeMarket();
    }

    public Response<Boolean> addExternalPurchaseService(String name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.addExternalPurchaseService(name);
    }

    public Response<Boolean> addExternalSupplyService(String name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.addExternalSupplyService(name);
    }

    public Response<Boolean> removeExternalPurchaseService(String name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.removeExternalPurchaseService(name);
    }

    @Override
    public Response<Boolean> removeExternalSupplyService(String name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.removeExternalSupplyService(name);
    }

    public Response<Boolean> purchaseShoppingCart(String username, String address, String purchase_service_name, String supply_service_name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
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
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.hasPurchaseService();
    }

    public Response<Boolean> hasSupplyService() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.hasSupplyService();
    }

    @Override
    public Response<Boolean> hasPurchaseService(String purchase_service_name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.hasPurchaseService(purchase_service_name);
    }

    @Override
    public Response<Boolean> hasSupplyService(String purchase_supply_name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.hasSupplyService(purchase_supply_name);
    }

    @Override
    public Response<Boolean> deleteUser(String name) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("You must be logged in in order to perform this action");
        }
        Response<Boolean> r = userFacade.isAdmin(user.getName());
        if (r.hadError() || !r.getObject()) {
            return new Response<>("Only admin users can perform this action");
        }
        Response<User> userResponse = userFacade.getUser(name);
        if (userResponse.hadError()) {
            return new Response<>(userResponse.getErrorMessage());
        }
        Response<Boolean> responseRemoveRoles = storeFacade.removeUserRoles(user, userResponse.getObject());
        if (responseRemoveRoles.hadError()) {
            return responseRemoveRoles;
        }
        return userFacade.removeUser(user.getName(), name);
    }

    @Override
    public Response<List<User>> getStoreManagers(int storeId){
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getManagers(user, storeId);
    }
}