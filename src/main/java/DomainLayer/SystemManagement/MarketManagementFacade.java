package DomainLayer.SystemManagement;

import DomainLayer.Response;
import DomainLayer.StoreFacade;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import Utility.LogUtility;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MarketManagementFacade
{
    private static class MarketManagementFacadeHolder
    {
        static final MarketManagementFacade INSTANCE = new MarketManagementFacade();
    }
    private MarketManagementFacade()
    {
        this.services = ExternalServicesHandler.getInstance();
        this.purchaseProcess = PurchaseProcess.getInstance();
        this.historyController = HistoryController.getInstance();
        this.notificationController = NotificationController.getInstance();
        this.storeController = StoreController.getInstance();
    }

    // Implementation of thread safe singleton
    public static MarketManagementFacade getInstance() {
        return MarketManagementFacadeHolder.INSTANCE;
    }
    public static final String GUEST_DEFAULT_NAME = "guest";
    private final ExternalServicesHandler services;
    private final PurchaseProcess purchaseProcess;
    private final HistoryController historyController;
    private final NotificationController notificationController;
    private final StoreController storeController;

    public Response<Boolean> clearAll()
    {
        this.notificationController.clearNotifications();
        this.historyController.clearHistory();
        this.services.clearServices();
        return new Response<>(true);
    }

    /***
     * The function responsible to initialize the connection with the external services, when the system is loaded
     * After this function, the system will have at least one supply service and one purchase service
     * @return Response - if the initialization succeeded or if there was an error
     */
    public Response<Boolean> initializeMarket()
    {
        try {
            // check if there is supply service - if not, add the first one
            if (!services.hasPurchaseService()) {
                services.addExternalPurchaseService("stub");
            }
            // check if there is purchase service - if not, add the first one
            if (!services.hasSupplyService()) {
                services.addExternalSupplyService("stub");
            }
            return new Response<>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * The user wants to pay about the items in his shopping cart.
     * @param user The user that wants to pay
     * @param address The shipping address, where to send the items.
     * @param purchase_service_name Which of the external purchase services is selected for this deal.
     * @param supply_service_name Which of the external supply services is selected for this deal.
     * @return Response - if the purchase process succeeded or if there was an error
     */
    public Response<Boolean> purchaseShoppingCart(User user, String address, String purchase_service_name, String supply_service_name)
    {
        try
        {
            List<ShoppingBasket> baskets = user.getCartBaskets();
            double price = this.purchaseProcess.CalcPrice(baskets);
            List<Map.Entry<Item, Integer>> items_and_amounts = this.purchaseProcess.getItemsAndAmounts(baskets);

            /* TODO:
             * 1. choose payment and shipping service
             * 2. check that every item matches the purchase and discount policy
             * 3. send the price and user details and store details to the purchase service
             * 4. should send money to the store owner here or is it the purchase services problem?
             * */

            this.services.pay(price, purchase_service_name);
            this.purchaseProcess.addToHistory(checkUsername(user), baskets);
            this.services.supply(address, items_and_amounts, supply_service_name);

            LogUtility.info("The user " + checkUsername(user) + " paid " + price + " shekels to the purchase services.");

            Set<Store> stores = baskets.stream().map(basket -> this.storeController.getStore(basket.getStoreId())).collect(Collectors.toSet());
            Map<Integer, List<String>> stores_and_owners = stores.stream().collect(Collectors.toMap(Store::getStoreId, Store::getOwners));
            this.notificationController.notifyStoresOwners(stores_and_owners, checkUsername(user));
            user.emptyShoppingCart();

            LogUtility.info("The ownerts of the stores " + stores.toString() + " received notification");

            return new Response<>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can add external purchase service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public Response<Boolean> addExternalPurchaseService(String name)
    {
        try {
            this.services.addExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can remove external purchase service, if the system has more than one purchase service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public Response<Boolean> removeExternalPurchaseService(String name)
    {
        try {
            this.services.removeExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can add external supply service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public Response<Boolean> addExternalSupplyService(String name)
    {
        try {
            this.services.addExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can remove external supply service, if the system has more than one supply service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public Response<Boolean> removeExternalSupplyService(String name)
    {
        try {
            this.services.removeExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains purchase services - for the tests
     * @return Response - true if there is at least one purchase service, false otherwise
     */
    public Response<Boolean> hasPurchaseService()
    {
        try {
            return new Response<>(this.services.hasPurchaseService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains supply services - for the tests
     * @return Response - true if there is at least one supply service, false otherwise
     */
    public Response<Boolean> hasSupplyService()
    {
        try {
            return new Response<>(this.services.hasSupplyService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains purchase services with the given name - for the tests
     * @return Response - true if the service was found, false otherwise
     */
    public Response<Boolean> hasPurchaseService(String purchase_service_name)
    {
        try {
            return new Response<>(this.services.hasPurchaseService(purchase_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains supply services with the given name - for the tests
     * @return Response - true if the service was found, false otherwise
     */
    public Response<Boolean> hasSupplyService(String supply_service_name)
    {
        try {
            return new Response<>(this.services.hasSupplyService(supply_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<History> getPurchaseHistory(String username)
    {
        try {
            return new Response<>(this.historyController.getPurchaseHistory(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<History> getStoreHistory(int store_id) {
        try {
            return new Response<>(this.historyController.getStoreHistory(store_id));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addNotification(String username, String message)
    {
        try
        {
            this.notificationController.addNotification(username, message);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeUserNotifications(String username)
    {
        try
        {
            this.notificationController.removeUserNotifications(username);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<INotification>> getUserNotifications(String username)
    {
        try
        {
            return new Response<>(this.notificationController.getUserNotifications(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    private String checkUsername(User user)
    {
        if (user.isSubscribed())
        {
            return user.getName();
        }
        else {
            return GUEST_DEFAULT_NAME;
        }
    }
}
