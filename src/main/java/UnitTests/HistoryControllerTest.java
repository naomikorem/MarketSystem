package UnitTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.Users.ShoppingBasket;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HistoryControllerTest extends AbstractTest
{
    private HistoryController historyController;
    private List<ShoppingBasket> baskets;
    private Set<ItemHistory> originals_as_items_history_store1;
    private Set<ItemHistory> originals_as_items_history_store5;
    private Set<ItemHistory> originals_as_items_history;
    private final String username;
    private final int store1_id;
    private final int store5_id;
    private final Date date;

    public HistoryControllerTest()
    {
        this.historyController = HistoryController.getInstance();
        this.username = "sagi is annoying";
        store1_id = 1;
        store5_id = 5;
        this.date = new Date();
    }

    @Before
    public void setup()
    {
        Item item1 = new Item("banana", Category.Food, 25);
        Item item2 = new Item("apple", Category.Food, 10);
        Item item3 = new Item("pineapple", Category.Food, 30);
        Item item4 = new Item("milk", Category.Food, 5.5);

        ShoppingBasket basket1 = new ShoppingBasket(store1_id);
        basket1.addItem(item1, 1);
        basket1.addItem(item2, 2);
        basket1.addItem(item3, 1);
        basket1.addItem(item4, 2);

        originals_as_items_history_store1 = basket1.getItemsAndAmounts().stream().
                map(item_amount ->convertItemToItemHistory(item_amount, store1_id, username, date)).collect(Collectors.toSet());

        Item item5 = new Item("shirt", Category.Clothing, 65);
        Item item6 = new Item("shoes", Category.Clothing, 200);
        Item item7 = new Item("socks", Category.Clothing, 30);
        Item item8 = new Item("pants", Category.Clothing, 75.9);

        ShoppingBasket basket2 = new ShoppingBasket(store5_id);
        basket2.addItem(item5, 5);
        basket2.addItem(item6, 3);
        basket2.addItem(item7, 1);
        basket2.addItem(item8, 3);

        originals_as_items_history_store5 = basket2.getItemsAndAmounts().stream().
                map(item_amount ->convertItemToItemHistory(item_amount, store5_id, username, date)).collect(Collectors.toSet());

        originals_as_items_history = new HashSet<>();
        originals_as_items_history.addAll(originals_as_items_history_store1);
        originals_as_items_history.addAll(originals_as_items_history_store5);

        baskets = new LinkedList<>();
        baskets.add(basket1);
        baskets.add(basket2);
    }

    @Test
    public void addItemsToUserHistory()
    {
        this.historyController.addToUserHistory(username, baskets, date);

        History res = this.historyController.getPurchaseHistory(username);
        Set<ItemHistory> items = res.getHistoryItems();

        assertEquals(items, originals_as_items_history);
    }

    @Test
    public void addItemsToStoreHistorySubscribedUser()
    {
        this.historyController.addToStoreHistory(username, baskets, date);

        History history_store_1 = this.historyController.getStoreHistory(store1_id);
        Set<ItemHistory> items_store_1 = history_store_1.getHistoryItems();

        History history_store_2 = this.historyController.getStoreHistory(store5_id);
        Set<ItemHistory> items_store_5 = history_store_2.getHistoryItems();

        assertEquals(items_store_1, originals_as_items_history_store1);
        assertEquals(items_store_5, originals_as_items_history_store5);
    }

    @Test
    public void addItemsToStoreHistoryUnsubscribedUser()
    {
        this.historyController.addToStoreHistory(HistoryController.GUEST_DEFAULT_NAME, baskets, date);

        History history_store_1 = this.historyController.getStoreHistory(store1_id);
        Set<ItemHistory> items_store_1 = history_store_1.getHistoryItems();
        Set<String> users_items = items_store_1.stream().map(item -> item.username).collect(Collectors.toSet());

        assertEquals(1, users_items.size());
        assertTrue(users_items.contains(HistoryController.GUEST_DEFAULT_NAME));
    }

    private static ItemHistory convertItemToItemHistory(Map.Entry<Item, Integer> item_amount, int store_id, String username, Date date)
    {
        Item item = item_amount.getKey();
        int amount = item_amount.getValue();
        return new ItemHistory(item.getId(), store_id, username, item.getProductName(), item.getCategory(), item.getPrice(), amount, date);
    }
}