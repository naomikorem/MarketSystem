package DomainLayer.SystemManagement;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchaseProcess
{
    private ExternalServicesHandler externalServicesHandler;
    //private NotificationFacade notificationFacade;
    private static class PurchaseProcessHolder {
        static final PurchaseProcess INSTANCE = new PurchaseProcess();
    }
    private PurchaseProcess()
    {
        this.externalServicesHandler = ExternalServicesHandler.getInstance();
        //this.notificationFacade = NotificationFacade.getInstance();
    }

    public static PurchaseProcess getInstance() {
        return PurchaseProcessHolder.INSTANCE;
    }
    public void handlePurchase(User user, String shipping_address, String purchase_service_name, String supply_service_name)
    {
        double price = 0;
        List<Map.Entry<Item, Integer>> items_and_amounts = new ArrayList<>();
        for (ShoppingBasket basket : user.getCartBaskets())
        {
            // every item has its own purchase type? yes

            price += basket.calculatePrice();
            items_and_amounts.addAll(basket.getItemsAndAmounts());

            // calculate discount percents
        }

        // how to choose the payment service
        // how to get payment details? get them or is it the external service problem?

        this.externalServicesHandler.pay(price, purchase_service_name);

        user.emptyShoppingCart();

        // get address from user

        this.externalServicesHandler.supply(shipping_address, items_and_amounts, supply_service_name);

        // save in purchase history

        /*for (ShoppingBasket basket : user.getCartBaskets())
        {
            int store_id = basket.getStoreId();

            this.notificationFacade.notifyOwners(store_id);
        }*/

        // for every basket in the shopping cart, find the store policy of discount and purchase
        // calculate the price needs to pay
        // find the purchase and supply services
        // call the purchase service with the relevant amount and user details
        // call the supply service with the relevant items
    }
}
