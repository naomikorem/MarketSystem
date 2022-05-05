package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PurchaseBadCasesTests extends AbstractTest
{
    private int store1_id;
    private int store2_id;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;

    // the successful item
    private Item item;
    private final int successfulPurchasedItemAmount;
    private final String successfulItemProductName;
    private final Category successfulItemCategory;

    private final String storeOwnerUsername;
    private final String storeOwnerPassword;
    private final String regularUsername;

    public PurchaseBadCasesTests()
    {
        this.storeOwnerUsername = "storeOwner111";
        this.storeOwnerPassword = "password";
        this.regularUsername = "regularUser222";

        this.successfulItemProductName = "Item";
        this.successfulItemCategory = Category.Food;
        this.successfulPurchasedItemAmount = 4;
    }

    @Before
    public void setup()
    {
        bridge.enter();
        bridge.register("storeOwner111@gmail.com", storeOwnerUsername, storeOwnerPassword);

        assertFalse(bridge.login(storeOwnerUsername, storeOwnerPassword).hadError());
        store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        item1 = bridge.addItemToStore(store1_id, "Item1", "Food", 10, 10).getObject();
        int item1_id = item1.getId();
        item2 = bridge.addItemToStore(store1_id, "Item2", "Food", 8, 6).getObject();
        int item2_id = item2.getId();

        store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        item3 = bridge.addItemToStore(store2_id, "Item3", "Food", 10, 10).getObject();
        int item3_id = item3.getId();
        item4 = bridge.addItemToStore(store2_id, "Item4", "Food", 8, 6).getObject();
        int item4_id = item4.getId();

        item = bridge.addItemToStore(store2_id, successfulItemProductName, "Food", 8, 6).getObject();
        int item_id = item.getId();
        bridge.logout();

        bridge.register("regularUser222@gmail.com", regularUsername, "password");
        this.bridge.login(regularUsername, "password");

        // one successful purchase
        this.bridge.addItemToCart(store2_id, item_id, successfulPurchasedItemAmount);
        bridge.purchaseShoppingCart("address", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);

        // prepare unsuccessful purchase
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.addItemToCart(store2_id, item3_id, 3);
        this.bridge.addItemToCart(store2_id, item4_id, 4);

//        this.originals_as_items_history_store1_user1 = new HashSet<>();
//        originals_as_items_history_store1_user1.add(convertItemToItemHistory(item1, 1, store1_id, regular_username1));
//        originals_as_items_history_store1_user1.add(convertItemToItemHistory(item2, 2, store1_id, regular_username1));
//
//        this.originals_as_items_history_store2_user2 = new HashSet<>();
//        originals_as_items_history_store2_user2.add(convertItemToItemHistory(item3, 1, store2_id, regular_username2));
//        originals_as_items_history_store2_user2.add(convertItemToItemHistory(item4, 2, store2_id, regular_username2));

    }



    @Test
    public void FailedToPay_PurchaseServiceDoesntExist()
    {
        Response<Boolean> res = this.bridge.purchaseShoppingCart("address", "not existing purchase service", AbstractProxy.GOOD_STUB_NAME);
        assertTrue(res.hadError());
    }

    @Test
    public void FailedToPay_SupplyServiceDoesntExist()
    {
        Response<Boolean> res = this.bridge.purchaseShoppingCart("address", AbstractProxy.GOOD_STUB_NAME, "not existing supply service");
        assertTrue(res.hadError());
    }

    @Test
    public void FailedToPay_PurchaseServiceFailedToCharge()
    {
        Response<Boolean> res = this.bridge.purchaseShoppingCart("address", AbstractProxy.BAD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);
        assertTrue(res.hadError());
    }

    @Test
    public void FailedToPay_SupplyServiceFailedToSupply()
    {
        Response<Boolean> res = this.bridge.purchaseShoppingCart("address", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.BAD_STUB_NAME);
        assertTrue(res.hadError());
    }

    @Test
    public void FailedToPay_EmptyShoppingCart()
    {
        // how to empty the shopping cart

//        Response<Boolean> res = this.bridge.purchaseShoppingCart("address", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.BAD_STUB_NAME);
//        assertTrue(res.hadError());
    }

    @After
    public void savedPreviousStateCheck()
    {
        // here, the user should have bought "item" in the first successful purchase,
        // but, should have been failed to buy other items in the second purchase

        // check that if the payment failed than there is no history of the items in the user history.
        Response<History> historyResponse = bridge.getPurchaseHistory();
        assertFalse(historyResponse.hadError());
        assertEquals(1, historyResponse.getObject().getHistoryItems().size());

        ItemHistory itemHistory = historyResponse.getObject().getHistoryItems().iterator().next();
        assertEquals(itemHistory.username, regularUsername);
        assertEquals(itemHistory.amount, successfulPurchasedItemAmount);
        assertEquals(itemHistory.product_name, successfulItemProductName);
        assertEquals(itemHistory.store_id, store2_id);
        assertEquals(itemHistory.category, successfulItemCategory);

        // check that if the payment failed than the shopping cart is not empty.
        Response<List<Item>> shoppingCartItems = bridge.getShoppingCartItems();
        assertFalse(shoppingCartItems.hadError());
        assertEquals(4, shoppingCartItems.getObject().size());

        assertTrue(shoppingCartItems.getObject().contains(item1));
        assertTrue(shoppingCartItems.getObject().contains(item2));
        assertTrue(shoppingCartItems.getObject().contains(item3));
        assertTrue(shoppingCartItems.getObject().contains(item4));

        bridge.logout();

        bridge.login(storeOwnerUsername, storeOwnerPassword);
        // check that if the payment failed than there is no history of the items in the relevant stores
        Response<History> historyResponseStore1 = bridge.getStoreHistory(store1_id);
        assertTrue(historyResponseStore1.hadError()); // because the purchase history of store 1 should be empty

        Response<History> historyResponseStore2 = bridge.getStoreHistory(store2_id);
        assertFalse(historyResponseStore2.hadError());
        assertEquals(1, historyResponseStore2.getObject().getHistoryItems().size());

        ItemHistory itemHistoryStore = historyResponseStore2.getObject().getHistoryItems().iterator().next();
        assertEquals(itemHistoryStore.username, regularUsername);
        assertEquals(itemHistoryStore.amount, successfulPurchasedItemAmount);
        assertEquals(itemHistoryStore.product_name, successfulItemProductName);
        assertEquals(itemHistoryStore.store_id, store2_id);
        assertEquals(itemHistoryStore.category, successfulItemCategory);

        // check that if the payment failed than the stores owners didn't receive notification.
        Response<List<INotification>> notificationResponse = bridge.getUserNotifications();
        assertEquals(1, notificationResponse.getObject().size());

        bridge.logout();
    }

    // move here the success payment test from PurchaseHistoryTests and rename the other class
}
