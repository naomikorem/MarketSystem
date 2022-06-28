package DomainLayer.SystemManagement;

import DomainLayer.Observer;
import DomainLayer.Response;
import DomainLayer.Stores.Bid;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.SupplyParamsDTO;
import ServiceLayer.DTOs.PaymentParamsDTO;
import Utility.LogUtility;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MarketManagementFacade {
    public static final String GUEST_DEFAULT_NAME = "guest";
    private final ExternalServicesHandler services;
    private final HistoryController historyController;
    private final NotificationController notificationController;
    private final StoreController storeController;


    private static class MarketManagementFacadeHolder {
        static final MarketManagementFacade INSTANCE = new MarketManagementFacade();
    }

    private MarketManagementFacade() {
        this.services = ExternalServicesHandler.getInstance();
        this.historyController = HistoryController.getInstance();
        this.notificationController = NotificationController.getInstance();
        this.storeController = StoreController.getInstance();

        this.services.loadServices();
        this.notificationController.loadNotifications();
        this.historyController.loadHistory();

        initializeMarket();
    }

    // Implementation of thread safe singleton
    public static MarketManagementFacade getInstance() {
        return MarketManagementFacadeHolder.INSTANCE;
    }

    /***
     * Clear all the data structures
     * @return Response with true
     */
    public Response<Boolean> clearAll() {
        this.notificationController.clearNotifications();
        this.historyController.clearHistory();
        this.services.clearServices();
        initializeMarket();
        return new Response<>(true);
    }

    /***
     * The function responsible to initialize the connection with the external services, when the system is loaded
     * After this function, the system will have at least one supply service and one purchase service
     * @return Response - if the initialization succeeded or if there was an error
     */
    @SneakyThrows
    public synchronized void initializeMarket() {
        // check if there is supply service - if not, add the first one
        if (!services.hasPurchaseService()) {
            services.addExternalPurchaseService(AbstractProxy.WSEP_PAYMENT, AbstractProxy.WSEP_PAYMENT_URL);
        }
        // check if there is purchase service - if not, add the first one
        if (!services.hasSupplyService()) {
            services.addExternalSupplyService(AbstractProxy.WSEP_SUPPLY, AbstractProxy.WSEP_SUPPLY_URL);
        }
    }

    /***
     * The user wants to pay about the items in his shopping cart.
     * @param user The user that wants to pay
     //* @param address The shipping address, where to send the items.
     //* @param purchase_service_name Which of the external purchase services is selected for this deal.
     //* @param supply_service_name Which of the external supply services is selected for this deal.
     * @return Response - if the purchase process succeeded or if there was an error
     */
    public Response<Boolean> purchaseShoppingCart(User user, PaymentParamsDTO paymentParamsDTO, SupplyParamsDTO supplyParamsDTO) {
        /* TODO:
         * 1. choose payment and shipping service
         * 2. check that every item matches the purchase and discount policy
         * 3. send the price and user details and store details to the purchase service
         * 4. should send money to the store owner here or is it the purchase services problem?
         * */
        try {
            String username = checkUsername(user);
            List<ShoppingBasket> baskets = user.getCartBaskets();
            Collection<Bid> bids = user.getBidsInCart();
            if (baskets.isEmpty() && bids.isEmpty()) {
                LogUtility.error("User " + username + " tried to purchase empty shopping cart");
                return new Response<>("Purchase shopping cart cannot be empty");
            }

            double price = calculateShoppingCartPrice(baskets);
            price+= calculateBidsPrice(bids);
            storeController.addBidsToBaskets(baskets, bids);
            List<Map.Entry<Item, Integer>> items_and_amounts = PurchaseProcess.getItemsAndAmounts(baskets);
            if (!this.services.supply(supplyParamsDTO, items_and_amounts)) {
                LogUtility.error("The user " + username + " couldn't get confirmation from the supply services.");
                return new Response<>("The purchase process canceled - couldn't contact the supply service.");
            }

            // do not move payment up: so we won't charge user before other checks
            if (!this.services.pay(price, paymentParamsDTO)) {
                LogUtility.error("The user " + username + " couldn't pay to the purchase services.");
                return new Response<>("The purchase process canceled - couldn't contact the purchase service.");
            }
            emptyShoppingCartAbdBids(user, bids);
            PurchaseProcess.addToHistory(username, baskets);

            Set<Store> stores = baskets.stream().map(basket -> this.storeController.getStore(basket.getStoreId())).collect(Collectors.toSet());
            Map<Integer, List<String>> stores_and_owners = stores.stream().collect(Collectors.toMap(Store::getStoreId, Store::getOwners));
            this.notificationController.notifyStoresOwners(stores_and_owners, username);
            LogUtility.info("Purchase process of shopping cart that the user " + username + " owned ended successfully");

            return new Response<>(true);
        } catch (Exception e) {
            //throw new IllegalArgumentException(e.getMessage());
            return new Response<>(e.getMessage());
        }
    }

    private void emptyShoppingCartAbdBids(User user, Collection<Bid> bids) {
        for( Bid b : bids){
            if(b.isInCart())
                storeController.removeBid(b.getStore(), user, b.getId());
        }
        user.emptyShoppingCart();
    }

    /***
     * Calculate the price of the baskets in the user's shopping cart
     * @param baskets The baskets that the user bought
     * @return double represents the price
     */
    public double calculateShoppingCartPrice(List<ShoppingBasket> baskets) {
        List<Double> basket_prices = baskets.stream().map(b -> storeController.getShoppingBasketPrice(b)).collect(Collectors.toList());
        return basket_prices.stream().reduce(0.0, (subtotal, element) -> subtotal + element);
    }
    private double calculateBidsPrice(Collection<Bid> bids) {
        List<Double> prices = bids.stream().map(Bid::getBidPrice).collect(Collectors.toList());
        return prices.stream().reduce(0.0, Double::sum);
    }

    public Response<Double> calculateShoppingCartPriceResult(List<ShoppingBasket> baskets) {
        try {
            List<Double> basket_prices = baskets.stream().map(b -> storeController.getShoppingBasketPrice(b)).collect(Collectors.toList());
            double price = basket_prices.stream().reduce(0.0, (subtotal, element) -> subtotal + element);
            return new Response<>(price);
        } catch (Exception e) {
        return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can add external purchase service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public Response<Boolean> addExternalPurchaseService(String name, String url) {
        try {
            this.services.addExternalPurchaseService(name, url);
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
    public Response<Boolean> removeExternalPurchaseService(String name) {
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
    public Response<Boolean> addExternalSupplyService(String name, String url) {
        try {
            this.services.addExternalSupplyService(name, url);
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
    public Response<Boolean> removeExternalSupplyService(String name) {
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
    public Response<Boolean> hasPurchaseService() {
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
    public Response<Boolean> hasSupplyService() {
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
    public Response<Boolean> hasPurchaseService(String purchase_service_name) {
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
    public Response<Boolean> hasSupplyService(String supply_service_name) {
        try {
            return new Response<>(this.services.hasSupplyService(supply_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> getAllExternalSupplyServicesNames()
    {
        try {
            return new Response<>(this.services.getAllExternalSupplyServicesNames());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<String>> getAllExternalPurchaseServicesNames()
    {
        try {
            return new Response<>(this.services.getAllExternalPurchaseServicesNames());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Receive the purchase history of a subscribed user.
     * @param username The name of the requested user
     * @return Response of the History of the given user - if found in users_history
     */
    public Response<History> getPurchaseHistory(String username) {
        try {
            return new Response<>(this.historyController.getPurchaseHistory(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Receive the purchase history of a given store
     * @param store_id The requested store
     * @return Response of the History of the relevant store, if exists in store_history
     */
    public Response<History> getStoreHistory(int store_id) {
        try {
            return new Response<>(this.historyController.getStoreHistory(store_id));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> attachObserver(Observer observer) {
        try {
            this.notificationController.attachObserver(observer);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> detachObserver(Observer observer) {
        try {
            this.notificationController.detachObserver(observer);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> notifyUsers(List<String> users_to_notify, String message) {
        try {
            this.notificationController.notifyUsers(users_to_notify, message);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Add notification to given user
     * @param username The given user
     * @param message The message of the notification
     */
    /*public Response<Boolean> addNotification(String username, String message)
    {
        try
        {
            this.notificationController.addNotification(username, message);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }*/

    /***
     * Remove all the notifications of given user
     * @param username The given user
     */
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

    /***
     * Receive all the notifications of some user
     * @param username The given user
     * @return List of notifications
     */
    public Response<List<INotification>> getUserNotifications(String username) {
        try {
            return new Response<>(this.notificationController.getUserNotifications(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if a given user is subscribed or not
     * @param user The given user to check
     * @return If the user is guest - default guest name, if the user is subscribed - his username
     */
    private String checkUsername(User user) {
        if (user.isSubscribed()) {
            return user.getName();
        } else {
            return GUEST_DEFAULT_NAME;
        }
    }
}
