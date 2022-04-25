import PurchaseServices.IExternalPurchaseService;
import SupplyServices.IExternalSupplyService;

public class PurchaseProcess
{
    public void Pay(User user)
    {
        // for every basket in the shopping cart, find the store policy of discount and purchase
        // calculate the price needs to pay
        // find the purchase and supply services
        // call the purchase service with the relevant amount and user details
        // call the supply service with the relevant items
    }

    public float getBasketPrice(int store_id, ShoppingBasket basket)
    {

    }

    public void Pay(IExternalPurchaseService payment_service, float amount, User user)
    {

    }

    public void supply(IExternalSupplyService supply_service, ShoppingBasket basket)
    {

    }
}
