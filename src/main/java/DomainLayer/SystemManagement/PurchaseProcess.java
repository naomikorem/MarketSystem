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

    public static void addToHistory(String username, List<ShoppingBasket> baskets)
    {
        Date purchase_date = new Date();
        if (!username.equals(GUEST_DEFAULT_NAME))
        {
            HistoryController.getInstance().addToUserHistory(username, baskets, purchase_date);
        }
        HistoryController.getInstance().addToStoreHistory(username, baskets, purchase_date);

    }

    public static double CalcPrice(List<ShoppingBasket> baskets) {
        double price = 0;
        for (ShoppingBasket basket : baskets)
        {
            price += basket.calculatePrice();

            // TODO: calculate discount percents
        }
        return price;
    }

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
