package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class HistoryTests extends AbstractTest
{
    private final String regular_username1;
    private final String regular_username2;
    private final String store1_owner_username;
    private final String store2_owner_username;

    private final String admin_username;
    private int store1_id;
    private int store2_id;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private int item1_id;
    private int item2_id;
    private int item3_id;
    private int item4_id;
    private Set<ItemHistory> originals_as_items_history_store1_user1;
    private Set<ItemHistory> originals_as_items_history_store2_user2;

    private final Date date;

    public HistoryTests()
    {
        super();
        this.regular_username1 = "regularUser1";
        this.regular_username2 = "regularUser2";
        this.store1_owner_username = "store1owner";
        this.store2_owner_username = "store2owner";
        this.admin_username = "admin";
        this.store1_id = 1;
        this.store2_id = 2;
        this.date = new Date();
    }

    @Before
    public void setup()
    {
        //bridge.initializeMarket();
        bridge.enter();
        bridge.register("user111@gmail.com", regular_username1, "first","last","password");
        bridge.register("user112@gmail.com", regular_username2,"first","last", "password");
        bridge.register("user222@gmail.com", store1_owner_username, "first","last","password");
        bridge.register("user223@gmail.com", store2_owner_username, "first","last","password");

        assertFalse(bridge.login(store1_owner_username, "password").hadError());
        this.store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        this.item1 = bridge.addItemToStore(store1_id, "Item1", "Food", 10, 10).getObject();
        this.item1_id = this.item1.getId();
        this.item2 = bridge.addItemToStore(store1_id, "Item2", "Food", 8, 6).getObject();
        this.item2_id = this.item2.getId();
        bridge.logout();

        assertFalse(bridge.login(store2_owner_username, "password").hadError());
        this.store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        this.item3 = bridge.addItemToStore(store2_id, "Item3", "Food", 10, 10).getObject();
        this.item3_id = this.item3.getId();
        this.item4 = bridge.addItemToStore(store2_id, "Item4", "Food", 8, 6).getObject();
        this.item4_id = this.item4.getId();
        bridge.logout();

        this.bridge.login(regular_username1, "password");
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.purchaseShoppingCart("ashdod", "stub", "stub");
        this.bridge.logout();

        this.bridge.login(regular_username2, "password");
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);
        this.bridge.purchaseShoppingCart("ashdod", "stub", "stub");
        this.bridge.logout();

        this.originals_as_items_history_store1_user1 = new HashSet<>();
        originals_as_items_history_store1_user1.add(convertItemToItemHistory(item1, 1, store1_id, regular_username1));
        originals_as_items_history_store1_user1.add(convertItemToItemHistory(item2, 2, store1_id, regular_username1));

        this.originals_as_items_history_store2_user2 = new HashSet<>();
        originals_as_items_history_store2_user2.add(convertItemToItemHistory(item3, 1, store2_id, regular_username2));
        originals_as_items_history_store2_user2.add(convertItemToItemHistory(item4, 2, store2_id, regular_username2));

    }

    @Test
    public void GuestReceivePersonalPurchaseHistoryTest()
    {
        Response<History> res = this.bridge.getPurchaseHistory();
        assertTrue(res.hadError());
    }

    @Test
    public void SubscriberReceivePersonalPurchaseHistoryTest()
    {
        this.bridge.login(regular_username1, "password");

        Response<History> res = this.bridge.getPurchaseHistory();
        assertFalse(res.hadError());

        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertEquals(items, originals_as_items_history_store1_user1);

        this.bridge.logout();
    }

    @Test
    public void AdminReceiveUsersPurchaseHistory()
    {
        this.bridge.login(admin_username, "admin");

        Response<History> res = this.bridge.getPurchaseHistory(regular_username1);
        assertFalse(res.hadError());

        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertEquals(items, originals_as_items_history_store1_user1);

        this.bridge.logout();
    }

    @Test
    public void AdminReceiveNotSubscribedUsersPurchaseHistory()
    {
        this.bridge.login(admin_username, "admin");

        Response<History> res = this.bridge.getPurchaseHistory("notInTheSystem");
        assertTrue(res.hadError());
        this.bridge.logout();
    }

    @Test
    public void storeOwnerReceivePurchaseHistoryTest()
    {
        this.bridge.login(store1_owner_username, "password");
        Response<History> res = this.bridge.getStoreHistory(store1_id);
        assertFalse(res.hadError());

        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertEquals(items, originals_as_items_history_store1_user1);

        // Try to get history of a store that you are not it's owner
        Response<History> res_error = this.bridge.getStoreHistory(store2_id);
        assertTrue(res_error.hadError());

        this.bridge.logout();

    }

    @Test
    public void userReceiveStorePurchaseHistoryTest()
    {
        this.bridge.login(regular_username1, "password");

        // Try to get history of a store that you are not it's owner
        Response<History> res1 = this.bridge.getStoreHistory(store1_id);
        assertTrue(res1.hadError());

        Response<History> res2 = this.bridge.getStoreHistory(store2_id);
        assertTrue(res2.hadError());

        this.bridge.logout();
    }

    @Test
    public void adminReceiveStorePurchaseHistory()
    {
        this.bridge.login(admin_username, "admin");

        Response<History> res1 = this.bridge.getStoreHistory(store1_id);
        assertFalse(res1.hadError());
        Set<ItemHistory> items1 = res1.getObject().getHistoryItems();
        assertEquals(items1, originals_as_items_history_store1_user1);

        Response<History> res2 = this.bridge.getStoreHistory(store2_id);
        assertFalse(res2.hadError());
        Set<ItemHistory> items2 = res2.getObject().getHistoryItems();
        assertEquals(items2, originals_as_items_history_store2_user2);

        this.bridge.logout();
    }

    @Test
    public void successfulPurchaseTest()
    {
        this.bridge.login(regular_username1, "password");
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);

        // check that the payment and supply confirmed.
        Response<Boolean> purchase_res = this.bridge.purchaseShoppingCart("ashdod", "stub", "stub");
        assertFalse(purchase_res.hadError());
        assertTrue(purchase_res.getObject());

        // check that the user's shopping cart is now empty
        Response<List<Item>> shopping_cart_left_items_res = bridge.getShoppingCartItems();
        assertFalse(shopping_cart_left_items_res.hadError());
        assertTrue(shopping_cart_left_items_res.getObject().isEmpty());

        // check that the history contains all the items that the user bought
        Set<ItemHistory> copy = new HashSet<>(originals_as_items_history_store1_user1);
        copy.add(convertItemToItemHistory(item3, 1, store2_id, regular_username1));
        copy.add(convertItemToItemHistory(item4, 2, store2_id, regular_username1));
        Response<History> res = this.bridge.getPurchaseHistory();
        assertFalse(res.hadError());
        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertEquals(items, copy);

        this.bridge.logout();
    }

    private ItemHistory convertItemToItemHistory(Item item, int amount, int store_id, String username)
    {
        return new ItemHistory(item.getId(), store_id, username, item.getProductName(), item.getCategory(), item.getPrice(), amount, date);
    }
}
