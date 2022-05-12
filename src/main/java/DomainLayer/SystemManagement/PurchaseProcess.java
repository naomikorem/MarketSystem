package DomainLayer.SystemManagement;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.Users.ShoppingBasket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PurchaseProcess
{
    public static final String GUEST_DEFAULT_NAME = "guest";

    /***
     * Add history of the items that the user just purchased.
     * @param username The username that made the purchase
     * @param baskets The baskets that the user bought
     */
    public static void addToHistory(String username, List<ShoppingBasket> baskets)
    {
        Date purchase_date = new Date();
        if (!username.equals(GUEST_DEFAULT_NAME))
        {
            // If its subscribed user than save his personal purchase history
            HistoryController.getInstance().addToUserHistory(username, baskets, purchase_date);
        }
        HistoryController.getInstance().addToStoreHistory(username, baskets, purchase_date);
    }

    /***
     * Calculate the price of the baskets in the user's shopping cart
     * @param baskets The baskets that the user bought
     * @return double represents the price
     */
    /*public static double CalcPrice(List<ShoppingBasket> baskets)
    {
        double price = 0;
        for (ShoppingBasket basket : baskets)
        {
            price += basket.calculatePrice();

            // TODO: calculate discount percents
        }
        return price;
    }*/

    /***
     * Return list of items and the amount that the user contains from each item in his shopping cart
     * @param baskets The baskets that the user bought
     * @return items and matching amounts
     */
    public static List<Map.Entry<Item, Integer>> getItemsAndAmounts(List<ShoppingBasket> baskets)
    {
        List<Map.Entry<Item, Integer>> items_and_amounts = new ArrayList<>();
        for (ShoppingBasket basket : baskets)
        {
            items_and_amounts.addAll(basket.getItemsAndAmounts());
        }
        return items_and_amounts;
    }
}
