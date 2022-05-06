package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.TODO;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.*;

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
    public Response<Boolean> addManager(String manager, int storeId) {
        try {
            if (!userFacade.isExist(manager)) {
                throw new IllegalArgumentException(String.format("There is no user by the name of %s", manager));
            }
            return storeFacade.addManager(user, userFacade.getUser(manager).getObject(), storeId);
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
        Response<Store> response = storeFacade.closeStore(user, storeId);
        if (response.hadError()) {
            return new Response<>(response.getErrorMessage());
        }
        Store s = response.getObject();
        for (String manager : s.getManagers()) {
            Response<Boolean> msgResponse = marketManagementFacade.addNotification(manager, String.format("The store %s that is managed by you was shut down", s.getStoreId()));
            if (msgResponse.hadError() || !msgResponse.getObject()) {
                return msgResponse;
            }
        }
        for (String manager : s.getOwners()) {
            Response<Boolean> msgResponse = marketManagementFacade.addNotification(manager, String.format("The store %s that is owned by you was shut down", s.getStoreId()));
            if (msgResponse.hadError() || !msgResponse.getObject()) {
                return msgResponse;
            }
        }
        return new Response<>(true);
    }

    @Override
    public Response<Boolean> permanentlyCloseStore(int storeId) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<Boolean> r1 = userFacade.isAdmin(user.getName());
        if (r1.hadError() || !r1.getObject()) {
            return r1;
        }
        Response<Store> s = storeFacade.permanentlyCloseStore(storeId);
        if (s.hadError()) {
            return new Response<>(s.getErrorMessage());
        }
        List<String> toDelete = new ArrayList<>(s.getObject().getManagers());
        toDelete.addAll(s.getObject().getOwners());
        toDelete.remove(user.getName());
        for (String toRemove : toDelete) {
            Response<Boolean> r2 = userFacade.removeUser(user.getName(), toRemove);
            if (r2.hadError()) {
                return r2;
            }
        }
        return new Response<>(true);
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
        Response<Boolean> r2 = storeFacade.removeOwner(user, r.getObject(), storeId);
        if (r2.hadError() || !r2.getObject()) {
            return r2;
        }
        return marketManagementFacade.addNotification(toRemove, String.format("You were removed as an owner of store %s", storeId));
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
        Response<Boolean> r2 = storeFacade.removeManager(user, r.getObject(), storeId);
        if (r2.hadError() || !r2.getObject()) {
            return r2;
        }
        return marketManagementFacade.addNotification(toRemove, String.format("You were removed as a manager of store %s", storeId));
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

    @Override
    public Response<Item> modifyItem(int storeId, int itemId, String productName, String category, double price, List<String> keywords) {
        return storeFacade.modifyItem(user, storeId, itemId, productName, category, price, keywords);
    }

    @Override
    public Response<Map<Item, Integer>> getItems(int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.storeFacade.getItems(storeId);
    }


    @Override
    public Response<List<String>> getStoreOwners(int storeId) // check
    {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<Store> store = storeFacade.getStore(storeId);
        if (store.hadError()) {
            return new Response<>(store.getErrorMessage());
        }
        return new Response<>(store.getObject().getOwners());

    }

    public Response<Boolean> addExternalPurchaseService(String name, String url) {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return is_admin_response;
        }

        return this.marketManagementFacade.addExternalPurchaseService(name, url);
    }

    public Response<Boolean> addExternalSupplyService(String name, String url) {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return is_admin_response;
        }

        return this.marketManagementFacade.addExternalSupplyService(name, url);
    }

    public Response<Boolean> removeExternalPurchaseService(String name) {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return is_admin_response;
        }

        return this.marketManagementFacade.removeExternalPurchaseService(name);
    }

    @Override
    public Response<Boolean> removeExternalSupplyService(String name) {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return is_admin_response;
        }

        return this.marketManagementFacade.removeExternalSupplyService(name);
    }

    public Response<Boolean> purchaseShoppingCart(String address, String purchase_service_name, String supply_service_name) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }


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

        return this.marketManagementFacade.purchaseShoppingCart(user, address, purchase_service_name, supply_service_name);

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


    public Response<History> getPurchaseHistory()
    {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Enter the system properly and be subscribes in order get his purchase history");
        }

        return this.marketManagementFacade.getPurchaseHistory(user.getName());
    }

    public Response<History> getPurchaseHistory(String username)
    {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return new Response<>("The current user is not a system admin");

        }
        return this.marketManagementFacade.getPurchaseHistory(username);
    }

    public Response<History> getStoreHistory(int store_id)
    {
        Response<Boolean> is_owner_response = isLoggedInOwnerCheck(store_id);

        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if((is_admin_response.hadError() || !is_admin_response.getObject()) && (is_owner_response.hadError() || !is_owner_response.getObject()))
        {
            return new Response<>("The user is not an owner of the store and not an admin of the system");

        }
        return this.marketManagementFacade.getStoreHistory(store_id);
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
    public Response<Boolean> deleteAdmin(String name) {
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
        if (userResponse.getObject() == null)
            return new Response<>("not a valid user");
        Response<Boolean> response = userFacade.removeAdmin(userResponse.getObject().getName());
        if (response.hadError()) {
            return response;
        }
        return userFacade.removeUser(user.getName(), name);
    }
    @Override
    public Response<Boolean> addAdmin(String name) {
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
        if (userResponse.getObject() == null)
            return new Response<>("not a valid user");
        Response<Boolean> response = userFacade.addAdmin(userResponse.getObject().getName());
        if (response.hadError()) {
            return response;
        }
        return userFacade.removeUser(user.getName(), name);
    }

    @Override
    public Response<Boolean> hasAdmin()
    {
        return userFacade.hasAdmin();
    }


    private Response<Boolean> isLoggedInOwnerCheck(int store_id) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if(!user.isSubscribed())
        {
            return new Response<>("You must be logged in admin in order to perform this action");
        }

        String username;
        try {
            username = user.getName();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

        return storeFacade.isOwner(store_id, username);
    }

    private Response<Boolean> isLoggedInAdminCheck() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if(!user.isSubscribed())
        {
            return new Response<>("You must be logged in admin in order to perform this action");
        }

        String username;
        try {
            username = user.getName();
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }

        return userFacade.isAdmin(username);
    }

    @Override
    public Response<List<String>> getStoreManagers(int storeId){
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getManagers(user, storeId);
    }

    public Response<Permission> getManagersPermissions(int storeId, String managerName){
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getManagersPermissions(user, storeId, managerName);
    }

    @Override
    public Response<List<INotification>> getUserNotifications() {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return this.marketManagementFacade.getUserNotifications(user.getName());
    }

    public Response<Set<Item>> searchProducts(String productName, String category, List<String> keywords) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.searchProducts(productName, category, keywords);
    }

    public Response<Set<Item>> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.filterProdacts(items, upLimit, lowLimit, rating);
    }

    public Response<Boolean> setUserName(String newUserName) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        String oldName = user.getName();
        Response<Boolean> r1 = userFacade.setUserName(user,newUserName);
        if (r1.hadError() || !r1.getObject()) {
            return r1;
        }
        return storeFacade.applyChangeName(user, oldName, newUserName);
    }

}