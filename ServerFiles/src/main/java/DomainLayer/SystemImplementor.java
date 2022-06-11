package DomainLayer;

import DomainLayer.Stores.*;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.SupplyParamsDTO;
import ServiceLayer.DTOs.PaymentParamsDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

public class SystemImplementor implements SystemInterface {
    @TODO
    //move checks if user == null to the delegating class when sockets are added

    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private MarketManagementFacade marketManagementFacade;
    private NotificationController notificationController;
    private User user;

    //Add catch to every function here.

    public SystemImplementor() {
        this.userFacade = new UserFacade();
        this.storeFacade = new StoreFacade();
        this.marketManagementFacade = MarketManagementFacade.getInstance();
        this.notificationController = NotificationController.getInstance();
    }

    public void setSession(String sessionId, SimpMessagingTemplate template) {
        this.user.setSessionId(sessionId);
        this.user.setTemplate(template);
    }

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
    public Response<User> register(String email, String userName, String firstName, String lastName, String password) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return userFacade.register(email, userName, firstName, lastName, password);
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
            User old = this.user;
            this.user = r.getObject();
            setSession(old.getSessionId(), old.getTemplate());
            this.marketManagementFacade.attachObserver(this.user);
        } else {
            return r;
        }
        return new Response<>(this.user);
    }

    public Response<String> getToken() {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return userFacade.getToken(user.getName());
    }

    public Response<User> loginUserByToken(String token) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if (user.isSubscribed()) {
            return new Response<>("You have to log out before attempting to log in to a user.");
        }
        Response<User> r = userFacade.loginUserByToken(token);

        if (!r.hadError()) {
            this.user = r.getObject();
            this.marketManagementFacade.attachObserver(this.user);
        } else {
            return r;
        }
        return new Response<>(this.user);
    }

    @Override
    public Response<Boolean> logout() {
        if (this.user == null || !this.user.isSubscribed()) {
            return new Response<>("You have to be logged in to perform this action.");
        }
        User current = this.user;
        Response<Boolean> res = userFacade.logout(user.getName());
        if (res.getObject()) {
            User oldUser = this.user;
            this.user = new User(new GuestState());
            setSession(oldUser.getSessionId(), oldUser.getTemplate());
            this.marketManagementFacade.detachObserver(current);
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
    public Response<Collection<Store>> getAllOpenStores() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getAllOpenStores();
    }

    @Override
    public Response<Collection<Store>> getStoresBesidesPermanentlyClosed() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getStoresBesidesPermanentlyClosed();
    }

    public Response<Collection<Store>> getUsersStores() {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only Logged-in users can perform this action.");
        }
        List<Store> stores = new ArrayList<>();
        for (int id : user.getOwnedStores()) {
            Response<Store> r = storeFacade.getStore(id);
            if (!r.hadError()) {
                stores.add(r.getObject());
            }
        }
        for (int id : user.getManagedStores()) {
            Response<Store> r = storeFacade.getStore(id);
            if (!r.hadError()) {
                stores.add(r.getObject());
            }
        }
        return new Response<>(stores);
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
        Response<Boolean> notify_managers_response = marketManagementFacade.notifyUsers(s.getManagers(), String.format("The store %s that is managed by you was shut down", s.getStoreId()));
        if (notify_managers_response.hadError() || !notify_managers_response.getObject()) {
            return notify_managers_response;
        }
        Response<Boolean> notify_owners_response = marketManagementFacade.notifyUsers(s.getOwners(), String.format("The store %s that is owned by you was shut down", s.getStoreId()));
        if (notify_owners_response.hadError() || !notify_owners_response.getObject()) {
            return notify_owners_response;
        }
        /*for (String manager : s.getManagers()) {
            Response<Boolean> msgResponse = marketManagementFacade.addNotification(manager, );

        }*/
        /*for (String manager : s.getOwners()) {
            Response<Boolean> msgResponse = marketManagementFacade.addNotification(manager, String.format("The store %s that is owned by you was shut down", s.getStoreId()));
            if (msgResponse.hadError() || !msgResponse.getObject()) {
                return msgResponse;
            }
        }*/
        return new Response<>(true);
    }

    @Override
    public Response<Boolean> reopenStore(int storeId) {
        Response<Store> response = storeFacade.reopenStore(user, storeId);
        if (response.hadError()) {
            return new Response<>(response.getErrorMessage());
        }
        /*Store s = response.getObject();
        Response<Boolean> notify_managers_response = marketManagementFacade.notifyUsers(s.getManagers(), String.format("The store %s that is managed by you was shut down", s.getStoreId()));
        if (notify_managers_response.hadError() || !notify_managers_response.getObject()) {
            return notify_managers_response;
        }
        Response<Boolean> notify_owners_response = marketManagementFacade.notifyUsers(s.getOwners(), String.format("The store %s that is owned by you was shut down", s.getStoreId()));
        if (notify_owners_response.hadError() || !notify_owners_response.getObject()) {
            return notify_owners_response;
        }*/

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

        Response<Boolean> msgResponse = marketManagementFacade.notifyUsers(toDelete, String.format("The store %s that is managed by you was shut down permanently", storeId));
        if (msgResponse.hadError() || !msgResponse.getObject()) {
            return msgResponse;
        }
        /*for(String user_to_delete : toDelete)
        {
            Response<Boolean> msgResponse = marketManagementFacade.addNotification(user_to_delete, String.format("The store %s that is managed by you was shut down permanently", storeId));
            if (msgResponse.hadError() || !msgResponse.getObject()) {
                return msgResponse;
            }
        }*/

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
        List<String> user_to_notify_remove = new LinkedList<>();
        user_to_notify_remove.add(toRemove);
        return marketManagementFacade.notifyUsers(user_to_notify_remove, String.format("You were removed as an owner of store %s", storeId));
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
        List<String> user_to_notify_remove = new LinkedList<>();
        user_to_notify_remove.add(toRemove);
        return marketManagementFacade.notifyUsers(user_to_notify_remove, String.format("You were removed as a manager of store %s", storeId));
    }

    @Override
    public Response<Store> addNewStore(String name) {
        return storeFacade.addNewStore(user, name);
    }

    @Override
    public Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount) {
        return storeFacade.addItemToStore(user, storeId, name, category, price, amount);
    }

    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount) {
        return storeFacade.addItemToStore(user, storeId, name, category, price, amount);
    }

    @Override
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount) {
        return storeFacade.removeItemFromStore(user, storeId, itemId, amount);
    }

    @Override
    public Response<Item> modifyItem(int storeId, int itemId, String productName, String category, double price, int amount, List<String> keywords) {
        return storeFacade.modifyItem(user, storeId, itemId, productName, category, price, amount, keywords);
    }

    public Response<Item> setItemAmount(int storeId, int itemId, int amount) {
        return storeFacade.setItemAmount(user, storeId, itemId, amount);
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

    public Response<Boolean> purchaseShoppingCart(PaymentParamsDTO paymentParamsDTO, SupplyParamsDTO supplyParamsDTOS) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }

        for(ShoppingBasket b : user.getCartBaskets()) {
            Response<Boolean> isPolicyAllowed = storeFacade.getShoppingBasketPurchesPolicy(b);
            if(isPolicyAllowed.hadError() || !isPolicyAllowed.getObject()) {
                return new Response<>("policy not allowed");
            }
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

        return this.marketManagementFacade.purchaseShoppingCart(user, paymentParamsDTO, supplyParamsDTOS);

    }

    public Response<Double> calculateShoppingCartPriceResult(List<ShoppingBasket> baskets) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<List<ShoppingBasket>> res = getCartBaskets();
        if (res.hadError())
            return new Response<>(res.getErrorMessage());
        return this.marketManagementFacade.calculateShoppingCartPriceResult(res.getObject());
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
    public Response<List<String>> getAllExternalSupplyServicesNames() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.getAllExternalSupplyServicesNames();
    }

    @Override
    public Response<List<String>> getAllExternalPurchaseServicesNames() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return this.marketManagementFacade.getAllExternalPurchaseServicesNames();
    }


    public Response<History> getPurchaseHistory() {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Enter the system properly and be subscribes in order get his purchase history");
        }

        return this.marketManagementFacade.getPurchaseHistory(user.getName());
    }

    public Response<History> getPurchaseHistory(String username) {
        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if (is_admin_response.hadError() || !is_admin_response.getObject()) {
            return new Response<>("The current user is not a system admin");

        }
        return this.marketManagementFacade.getPurchaseHistory(username);
    }

    public Response<History> getStoreHistory(int store_id) {
        Response<Boolean> is_owner_response = isLoggedInOwnerCheck(store_id);

        Response<Boolean> is_admin_response = isLoggedInAdminCheck();
        if ((is_admin_response.hadError() || !is_admin_response.getObject()) && (is_owner_response.hadError() || !is_owner_response.getObject())) {
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
        if (!userResponse.getObject().getManagedStores().isEmpty()) {
            return new Response<>("The user is a manager of a store, can't be removed");
        }
        if (!userResponse.getObject().getOwnedStores().isEmpty()) {
            return new Response<>("The user is a store owner of a store, can't be removed");
        }
        //Response<Boolean> responseRemoveRoles = storeFacade.removeUserRoles(user, userResponse.getObject());
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
        return userFacade.addAdmin(userResponse.getObject().getName());
    }

    @Override
    public Response<Boolean> hasAdmin() {
        return userFacade.hasAdmin();
    }

    @Override
    public Response<Boolean> removeItemFromCart(int storeId, Item item, int amount) {
        try {
            if (user == null) {
                return new Response<>("Enter the system properly in order to perform actions in it.");
            }
            ShoppingBasket store_baskets = null;
            for (ShoppingBasket basket : user.getCartBaskets()) {
                if (basket.getStoreId() == storeId) {
                    store_baskets = basket;
                    break;
                }
            }
            if (store_baskets == null) {
                return new Response<>("Costumer have no basket in this store, cannot return items.");
            }
            if (!store_baskets.hasItem(item)) {
                return new Response<>("Costumer don't acquire this item in the basket of this store, cannot return items.");
            }
            if (amount <= 0) {
                return new Response<>("Returning amount should be a positive number");
            }
            if (amount > store_baskets.amountFromItem(item)) {
                return new Response<>("Returning amount cannot be larger then the amount in the basket");
            }

            for (int i = 0; i < amount; i++) {
                store_baskets.removeItem(item);
            }
            Response<Item> itemRes = storeFacade.returnItemToStore(storeId, item, amount);
            if (itemRes.hadError()) {
                return new Response<>(itemRes.getErrorMessage());
            }
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Boolean> removeItemIDFromCart(int storeId, int itemid, int amount) {
        try {
            if (user == null) {
                return new Response<>("Enter the system properly in order to perform actions in it.");
            }
            ShoppingBasket store_baskets = null;
            for (ShoppingBasket basket : user.getCartBaskets()) {
                if (basket.getStoreId() == storeId) {
                    store_baskets = basket;
                    break;
                }
            }
            if (store_baskets == null) {
                return new Response<>("Costumer have no basket in this store, cannot return items.");
            }
            Response<Item> res = storeFacade.reserveItemFromStore(storeId, itemid, 0);
            Item item = res.getObject();
            if (!store_baskets.hasItem(item)) {
                return new Response<>("Costumer don't acquire this item in the basket of this store, cannot return items.");
            }
            if (amount <= 0) {
                return new Response<>("Returning amount should be a positive number");
            }
            if (amount > store_baskets.amountFromItem(item)) {
                return new Response<>("Returning amount cannot be larger then the amount in the basket");
            }

            for (int i = 0; i < amount; i++) {
                store_baskets.removeItem(item);
            }
            Response<Item> itemRes = storeFacade.returnItemToStore(storeId, item, amount);
            if (itemRes.hadError()) {
                return new Response<>(itemRes.getErrorMessage());
            }
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private Response<Boolean> isLoggedInOwnerCheck(int store_id) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if (!user.isSubscribed()) {
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

    public Response<Boolean> isLoggedInAdminCheck() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        if (!user.isSubscribed()) {
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
    public Response<User> getUser(String userName) {
        Response<Boolean> r1 = isLoggedInAdminCheck();
        if (r1.hadError()) {
            return new Response<>("In order to perform this action you must be an Admin");
        }
        Response<User> userResponse = userFacade.getUser(userName);
        if (userResponse.hadError()) {
            return new Response<>(userResponse.getErrorMessage());
        }
        if (userResponse.getObject() == null)
            return new Response<>("not a valid user");
        return userResponse;
    }

    @Override
    public Response<List<String>> getStoreManagers(int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getManagers(user, storeId);
    }

    public Response<Permission> getManagersPermissions(int storeId, String managerName) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getManagersPermissions(user, storeId, managerName);
    }

    @Override
    public Response<Boolean> removeUserNotifications() {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return this.marketManagementFacade.removeUserNotifications(user.getName());
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

    public Response<Double> getItemRating(int storeId, int itemId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getRatingOfProduct(storeId, itemId);
    }

    public Response<Boolean> setItemRating(int storeId, int itemId, double rate) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.setRatingOfProduct(storeId, itemId, rate);
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
        Response<Boolean> r1 = userFacade.setUserName(user, newUserName);
        if (r1.hadError() || !r1.getObject()) {
            return r1;
        }
        return storeFacade.applyChangeName(user, oldName, newUserName);
    }

    public Response<SimpleDiscountPolicy> addDiscount(int storeId, double percentage) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addDiscount(user, storeId, percentage);
    }

    public Response<List<SimpleDiscountPolicy>> getAllDiscountPolicies(int storeId) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.getAllDiscountPolicies(user, storeId);
    }

    public Response<List<SimplePurchasePolicy>> getAllPurchasePolicies(int storeId) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.getAllPurchasePolicies(user, storeId);
    }

    public Response<SimplePurchasePolicy> addPolicy(int storeId, int hour, Calendar date) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addPolicy(user, storeId, hour, date);
    }

    public Response<SimpleDiscountPolicy> addExclusiveDiscount(int storeId, double percentage) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addExclusiveDiscount(user, storeId, percentage);
    }

    public Response<AbstractDiscountPolicy> addItemPredicateToDiscount(int storeId, int discountId, String type, int itemId) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addItemPredicateToDiscount(user, storeId, discountId, type, itemId);
    }

    public Response<AbstractPurchasePolicy> addItemPredicateToPolicy(int storeId, int policyId, String type, int itemId, int hour) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addItemPredicateToPolicy(user, storeId, policyId, type, itemId, hour);
    }

    @Override
    public Response<AbstractPurchasePolicy> addItemNotAllowedInDatePredicateToPolicy(int storeId, int policyId, String type, int itemId, Calendar date) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addItemNotAllowedInDatePredicateToPolicy(user, storeId, policyId, type, itemId, date);
    }

    public Response<AbstractDiscountPolicy> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addCategoryPredicateToDiscount(user, storeId, discountId, type, categoryName);
    }

    public Response<AbstractDiscountPolicy> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.addBasketRequirementPredicateToDiscount(user, storeId, discountId, type, minPrice);
    }

    public Response<Boolean> changeDiscountPercentage(int storeId, int discountId, double newPercentage) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.changeDiscountPercentage(user, storeId, discountId, newPercentage);
    }

    public Response<Boolean> changePolicyHour(int storeId, int policyId, int newHour, Calendar newDate) {
        if (user == null || !user.isSubscribed()) {
            return new Response<>("Only logged in users can perform this action.");
        }
        return storeFacade.changePolicyHour(user, storeId, policyId, newHour, newDate);
    }

    public Response<Double> getCartPrice() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        double total = 0;
        for (ShoppingBasket sb : user.getCartBaskets()) {
            Response<Double> response = storeFacade.getShoppingBasketPrice(sb);
            if (response.hadError()) {
                return response;
            }
            total += response.getObject();
        }
        return new Response<>(total);
    }

    public Response<Map<Item, Double>> getShoppingBasketDiscounts(ShoppingBasket sb) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getShoppingBasketDiscounts(sb);
    }


    public Response<Boolean> getIsLegalToPurchase(int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        for (ShoppingBasket sb : user.getCartBaskets()) {
            if (sb.getStoreId() == storeId) {
                Response<Boolean> response = storeFacade.getShoppingBasketPurchesPolicy(sb);
                if (response.hadError()) {
                    return response;
                }
                if (response.getObject() == false)
                    return new Response<>(false);
            }
        }
        return new Response<>(true);
    }

    public Response<Boolean> removeDiscount(int storeId, int discountId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.removeDiscount(user, storeId, discountId);
    }

    public Response<Boolean> removePolicy(int storeId, int policyId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.removePolicy(user, storeId, policyId);
    }

    public Response<String[]> getStoreNameByID(int id) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getStoreNameByID(id);
    }

    @Override
    public Response<Boolean> addBid(int storeId, double bidPrice, int item, int amount) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<Bid> bid_response = storeFacade.addBid(storeId, user.getName(), bidPrice, item, amount);
        if(bid_response.hadError())
            return new Response<>(bid_response.getErrorMessage());
        Bid  bid = bid_response.getObject();
        Response<Store> store_response = storeFacade.getStore(storeId);
        if(store_response.hadError())
            return new Response<>(store_response.getErrorMessage());
        Store  store  = store_response.getObject();
        user.addBid(bid);
        Response<Boolean> notify_managers_response = marketManagementFacade.notifyUsers(store.getManagers(), String.format("The Costumer %s had a new bidding in your store %s", bid.getCostumer(), store.getName()));
        if (notify_managers_response.hadError() || !notify_managers_response.getObject()) {
            return notify_managers_response;
        }
        Response<Boolean> notify_owners_response = marketManagementFacade.notifyUsers(store.getOwners(), String.format("The Costumer %s had a new bidding in your store %s", bid.getCostumer(), store.getName()));
        if (notify_owners_response.hadError() || !notify_owners_response.getObject()) {
            return notify_owners_response;
        }
        List<String> user_to_notify = new LinkedList<>();
        user_to_notify.add(user.getName());
        Response<Boolean> notify_costumer_response = marketManagementFacade.notifyUsers(user_to_notify, String.format("Successfully bid %s₪, on %s, from store %s", bid.getBidPrice(), bid.getItem(), store.getName()));
        if (notify_costumer_response.hadError() || !notify_costumer_response.getObject()) {
            return notify_costumer_response;
        }
        return new Response<>(true);
    }

    @Override
    public Response<Collection<Bid>> getBids(int storeId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.getBids(storeId, user.getName());
    }
    @Override
    public Response<Collection<Bid>> getUserBids() {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return new Response<>(user.getBids());
    }
    @Override
    public Response<Bid> approveBid(int storeId, int bidId) {
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        return storeFacade.approveBid(storeId, user, bidId);
    }
    @Override
    public Response<Bid> updateBid(int storeId,int bidId, double newPrice){
        if (user == null) {
            return new Response<>("Enter the system properly in order to perform actions in it.");
        }
        Response<Bid> response = storeFacade.removeBid(storeId, user, bidId);
        if(response.hadError())
            return new Response<>(response.getErrorMessage());
        Bid bid = response.getObject();
        Double oldPrice = bid.getBidPrice();
        user.removeBid(bid.getId());
        response = storeFacade.addBid(storeId, bid.getCostumer(), bid.getBidPrice(), bid.getItem(), bid.getAmount());
        if(response.hadError())
            return new Response<>(response.getErrorMessage());
        Response<Store> store_response = storeFacade.getStore(storeId);
        if(store_response.hadError())
            return new Response<>(store_response.getErrorMessage());
        Store  store  = store_response.getObject();
        user.addBid(bid);
        Response<Boolean> notify_managers_response = marketManagementFacade.notifyUsers(store.getManagers(), String.format("Bid update - Item %s from price %s₪ to %s₪ for costumer %s in store %s", bid.getItem(), oldPrice, bid.getBidPrice(), bid.getCostumer(),  store.getName()));
        if (notify_managers_response.hadError() || !notify_managers_response.getObject()) {
            return new Response<>( notify_managers_response.getErrorMessage());
        }
        Response<Boolean> notify_owners_response = marketManagementFacade.notifyUsers(store.getOwners(), String.format("Bid update - Item %s from price %s₪ to %s₪ for costumer %s in store %s", bid.getItem(),oldPrice, bid.getBidPrice(), bid.getCostumer(),  store.getName()));
        if (notify_owners_response.hadError() || !notify_owners_response.getObject()) {
            return new Response<>(notify_owners_response.getErrorMessage());
        }
        List<String> user_to_notify = new LinkedList<>();
        user_to_notify.add(user.getName());
        Response<Boolean> notify_costumer_response = marketManagementFacade.notifyUsers(user_to_notify,  String.format("Bid update - Item %s from price %s₪ to %s₪ in store %s", bid.getItem(), oldPrice, bid.getBidPrice(), bid.getCostumer(), store.getName()));
        if (notify_costumer_response.hadError() || !notify_costumer_response.getObject()) {
            return new Response<>( notify_costumer_response.getErrorMessage());
        }
        return response;
    }
}

