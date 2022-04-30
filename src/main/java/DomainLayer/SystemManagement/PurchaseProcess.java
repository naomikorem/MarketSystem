package DomainLayer.SystemManagement;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PurchaseProcess
{
    public static final String GUEST_DEFAULT_NAME = "guest";

    private HistoryController historyController;

    private static class PurchaseProcessHolder {
        static final PurchaseProcess INSTANCE = new PurchaseProcess();
    }
    private PurchaseProcess()
    {
        this.historyController = HistoryController.getInstance();
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
   /* public void handlePurchase(User user, String shipping_address, String purchase_service_name, String supply_service_name)
    {
        List<ShoppingBasket> baskets = user.getCartBaskets();
        double price = CalcPrice(baskets);
        List<Map.Entry<Item, Integer>> items_and_amounts = getItemsAndAmounts(baskets);

        *//* TODO:
        * 1. choose payment and shipping service
        * 2. check that every item matches the purchase and discount policy
        * 3. send the price and user details and store details to the purchase service
        * 4. should send money to the store owner here or is it the purchase services problem?
        * 5. foreach store owner send notifications about the bought items.
        * *//*

        Date purchase_date = new Date();
        //this.externalServicesHandler.pay(price, purchase_service_name);

        if (user.isSubscribed())
        {
            String username = user.getName();
            this.historyController.addToUserHistory(username, baskets, purchase_date);
            this.historyController.addToStoreHistory(username, baskets, purchase_date);
        }
        else {
            this.historyController.addToPurchaseStoreHistory(baskets, purchase_date);
        }
        user.emptyShoppingCart();

        // Call supply services with the relevant details.
        //this.externalServicesHandler.supply(shipping_address, items_and_amounts, supply_service_name);
    }*/

    public void addToHistory(String username, List<ShoppingBasket> baskets)
    {
        Date purchase_date = new Date();
        if (!username.equals(GUEST_DEFAULT_NAME))
        {
            this.historyController.addToUserHistory(username, baskets, purchase_date);
        }
        this.historyController.addToStoreHistory(username, baskets, purchase_date);

    }

    public double CalcPrice(List<ShoppingBasket> baskets) {
        double price = 0;
        for (ShoppingBasket basket : baskets)
        {
            price += basket.calculatePrice();

            // TODO: calculate discount percents
        }
        return price;
    }

    public List<Map.Entry<Item, Integer>> getItemsAndAmounts(List<ShoppingBasket> baskets)
    {
        List<Map.Entry<Item, Integer>> items_and_amounts = new ArrayList<>();
        for (ShoppingBasket basket : baskets)
        {
            items_and_amounts.addAll(basket.getItemsAndAmounts());
        }
        return items_and_amounts;
    }
}
