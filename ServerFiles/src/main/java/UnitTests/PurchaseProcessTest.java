package UnitTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.PurchaseProcess;
import DomainLayer.Users.ShoppingBasket;
import acceptenceTests.AbstractTest;
import org.apache.commons.collections.set.SynchronizedSet;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PurchaseProcessTest extends AbstractTest
{
    private ShoppingBasket basket1, basket2;
    private List<ShoppingBasket> baskets;
    private Map<Item, Integer> items_basket_1, items_basket_2;
    private Set<Item> items_history_basket_1, items_history_basket_2;
    private final int store1_id;
    private final int store5_id;
    private String username;
    private HistoryController historyController;

    public PurchaseProcessTest()
    {
        this.username = "sagi";
        this.store1_id = 1;
        this.store5_id = 5;
        this.basket1 = new ShoppingBasket(store1_id);
        this.basket2 = new ShoppingBasket(store5_id);
        this.items_basket_1 = new HashMap<>();
        this.items_basket_2 = new HashMap<>();
        this.items_history_basket_1 = new HashSet<>();
        this.items_history_basket_2 = new HashSet<>();
        this.baskets = new LinkedList<>();
        this.historyController = HistoryController.getInstance();
    }

    @Before
    public void setup()
    {
        Item item1 = new Item("banana", Category.Food, 25);
        Item item2 = new Item("apple", Category.Food, 10);
        Item item3 = new Item("pineapple", Category.Food, 30);
        Item item4 = new Item("milk", Category.Food, 5.5);

        basket1.addItem(item1, 1);
        basket1.addItem(item2, 2);
        this.items_basket_1.put(item1, 1);
        this.items_basket_1.put(item2, 2);

        Date date = new Date();
        items_history_basket_1 = basket1.getItems();

        basket2.addItem(item3, 5);
        basket2.addItem(item4, 3);
        this.items_basket_2.put(item3, 5);
        this.items_basket_2.put(item4, 3);

        items_history_basket_2 = basket2.getItems();

        baskets.add(basket1);
        baskets.add(basket2);
    }

    @Test
    public void addBasketsItemsToHistory()
    {
        PurchaseProcess.addToHistory(username, baskets);

        // check that the user contains the purchase history
        History user_history = this.historyController.getPurchaseHistory(username);
        Set<ItemHistory> user_items = user_history.getHistoryItems();
        Set<Item> real_user_items = new HashSet<>(this.items_history_basket_1);
        real_user_items.addAll(this.items_history_basket_2);
        assertTrue(compareHistoryItemsToRegularItems(user_items, real_user_items));

        // check that store 1 has all the purchase history
        Set<ItemHistory> store_1_history = this.historyController.getStoreHistory(store1_id).getHistoryItems();
        assertTrue(compareHistoryItemsToRegularItems(store_1_history, items_history_basket_1));
        // check that store 5 has all the purchase history
        Set<ItemHistory> store_5_history = this.historyController.getStoreHistory(store5_id).getHistoryItems();
        assertTrue(compareHistoryItemsToRegularItems(store_5_history, items_history_basket_2));
    }

    @Test
    public void getUserItemsAndAmounts()
    {
        List<Map.Entry<Item, Integer>> items_and_amounts_in_baskets = PurchaseProcess.getItemsAndAmounts(baskets);
        List<Map.Entry<Item, Integer>> real_items_and_amounts = new ArrayList<>();
        real_items_and_amounts.addAll(items_basket_1.entrySet());
        real_items_and_amounts.addAll(items_basket_2.entrySet());

        assertEquals(items_and_amounts_in_baskets.size(), 4);
        assertEquals(items_and_amounts_in_baskets, real_items_and_amounts);
    }
}
