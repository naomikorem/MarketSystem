package DomainLayer.SystemManagement;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchaseProcess
{
    private ExternalServicesHandler externalServicesHandler;
    private HistoryController historyController;

    //private NotificationFacade notificationFacade;
    private static class PurchaseProcessHolder {
        static final PurchaseProcess INSTANCE = new PurchaseProcess();
    }
    private PurchaseProcess()
    {
        this.externalServicesHandler = ExternalServicesHandler.getInstance();
        this.historyController = HistoryController.getInstance();
        //this.notificationFacade = NotificationFacade.getInstance();
    }

    // Implementation of thread safe singleton
    public static PurchaseProcess getInstance() {
        return PurchaseProcessHolder.INSTANCE;
    }

    /***
     * Handle the process of purchasing the shopping cart of a user, including taking care of the external services.
     * @param user The user that wants to purchase
     * @param shipping_address The user's address, shopping address for the items
     * @param purchase_service_name The selected purchase service
     * @param supply_service_name The selected supply service
     */
    public void handlePurchase(User user, String shipping_address, String purchase_service_name, String supply_service_name)
    {
        double price = 0;
        List<Map.Entry<Item, Integer>> items_and_amounts = new ArrayList<>();

        List<ShoppingBasket> baskets = user.getCartBaskets();

        // Calculate the price that the user needs to pay.
        for (ShoppingBasket basket : baskets)
        {
            price += basket.calculatePrice();
            items_and_amounts.addAll(basket.getItemsAndAmounts());

            // TODO: calculate discount percents
        }

        /* TODO:
        * 1. choose payment and shipping service
        * 2. check that every item matches the purchase and discount policy
        * 3. send the price and user details and store details to the purchase service
        * 4. should send money to the store owner here or is it the purchase services problem?
        * 5. foreach store owner send notifications about the bought items.
        * */

        this.externalServicesHandler.pay(price, purchase_service_name);

        if (user.isSubscribed()) {
            String username = user.getName();
            this.historyController.addToPurchaseHistory(username, baskets);
            this.historyController.addToStoreHistory(username, baskets);
        }

        this.historyController.addToPurchaseStoreHistory(baskets);

        user.emptyShoppingCart();

        // Call supply services with the relevant details.
        this.externalServicesHandler.supply(shipping_address, items_and_amounts, supply_service_name);

        // save in purchase history

        // send notifications about the items
        /*for (ShoppingBasket basket : user.getCartBaskets())
        {
            int store_id = basket.getStoreId();

            this.notificationFacade.notifyOwners(store_id);
        }*/
    }
}
