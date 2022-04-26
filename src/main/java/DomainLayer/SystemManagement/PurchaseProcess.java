package DomainLayer.SystemManagement;

import DomainLayer.StoreFacade;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.NotificationManager.NotificationFacade;
import DomainLayer.SystemManagement.PurchaseServices.IExternalPurchaseService;
import DomainLayer.SystemManagement.SupplyServices.IExternalSupplyService;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;

public class PurchaseProcess
{
    private ExternalServices externalServices;
    //private NotificationFacade notificationFacade;
    private static class PurchaseProcessHolder {
        static final PurchaseProcess INSTANCE = new PurchaseProcess();
    }
    private PurchaseProcess()
    {
        this.externalServices = ExternalServices.getInstance();
        //this.notificationFacade = NotificationFacade.getInstance();
    }

    public static PurchaseProcess getInstance() {
        return PurchaseProcessHolder.INSTANCE;
    }
    public void Pay(User user, String purchase_service_name, String supply_service_name)
    {
        double price = 0;
        for (ShoppingBasket basket : user.getbaskets())
        {
            // 3.1 The system checks that the basket follows the purchase rules of the store's purchase policy.
            // every item has its own purchase type?

            for (Item item : basket.getItems())
            {
                price += item.getPrice();
            }

            // calculate discount percents
        }

        // how to choose the payment service
        // how to get payment details? get them or is it the external service problem?

        this.externalServices.pay(price, purchase_service_name);

        // get address from user

        this.externalServices.supply(user.getAddress(), user.getShoppingCartItems(), supply_service_name);

        // save in purchase history

        /*for (ShoppingBasket basket : user.getbaskets())
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

    public float getBasketPrice(int store_id, ShoppingBasket basket)
    {
        return 0;
    }
}
