package UnitTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.Users.ShoppingBasket;
import acceptenceTests.AbstractTest;
import org.apache.commons.collections.set.SynchronizedSet;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryControllerTest extends AbstractTest
{
    private HistoryController historyController;
    private ShoppingBasket basket1;
    private ShoppingBasket basket2;
    private String username;

    public HistoryControllerTest()
    {
        this.historyController = HistoryController.getInstance();
        this.username = "sagi is annoying";
    }

    @Test
    public void addItemToUserHistory()
    {
        ShoppingBasket basket = new ShoppingBasket(1);
        Item item1 = new Item("banana", Category.Food, 25);
        Item item2 = new Item("apple", Category.Food, 10);
        Item item3 = new Item("pineapple", Category.Food, 30);
        Item item4 = new Item("milk", Category.Food, 5.5);

        basket.addItem(item1, 1);
        basket.addItem(item2, 2);
        basket.addItem(item3, 1);
        basket.addItem(item4, 2);

        Set<Item> original_items = new HashSet<>();
        original_items.add(item1);
        original_items.add(item2);
        original_items.add(item3);
        original_items.add(item4);

        List<ShoppingBasket> baskets = new LinkedList<>();
        baskets.add(basket);
        this.historyController.addToPurchaseHistory(username, baskets);
        History res = this.historyController.getPurchaseHistory(username);

        Set<ItemHistory> items = res.getHistoryItems();

        //Set<String> names = items.stream().map(item -> item.product_name).collect(Collectors.toSet());
        //assertTrue(names.contains(item1.getProductName()) && names.contains(item2.getProductName()) &&
        //        names.contains(item3.getProductName()) && names.contains(item4.getProductName()));

        assertTrue(items.containsAll(original_items));

    }

}