package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
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
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        bridge.enter();
        bridge.register("storeOwner111@gmail.com", storeOwnerUsername,"first","last", storeOwnerPassword);

        assertFalse(bridge.login(storeOwnerUsername, storeOwnerPassword).hadError());
        store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        item1 = bridge.addItemToStore(store1_id, "Item1", Category.Food, 10, 10).getObject();
        int item1_id = item1.getId();
        item2 = bridge.addItemToStore(store1_id, "Item2", Category.Food, 8, 6).getObject();
        int item2_id = item2.getId();

        store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        item3 = bridge.addItemToStore(store2_id, "Item3", Category.Food, 10, 10).getObject();
        int item3_id = item3.getId();
        item4 = bridge.addItemToStore(store2_id, "Item4", Category.Food, 8, 6).getObject();
        int item4_id = item4.getId();

        // the successful item
        Item item = bridge.addItemToStore(store2_id, successfulItemProductName, Category.Food, 8, 6).getObject();
        int item_id = item.getId();
        bridge.logout();

        bridge.register("regularUser222@gmail.com", regularUsername,"first","last", "password");
        this.bridge.login(regularUsername, "password");

        // one successful purchase
        this.bridge.addItemToCart(store2_id, item_id, successfulPurchasedItemAmount);
        bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);

        // prepare unsuccessful purchase
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.addItemToCart(store2_id, item3_id, 3);
        this.bridge.addItemToCart(store2_id, item4_id, 4);
    }

    @Test
    public void FailedToPay_PurchaseServiceDoesntExist()
    {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                "not existing purchase service",
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        Response<Boolean> res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertTrue(res.hadError());
        savedPreviousStateCheck();
    }

    @Test
    public void FailedToPay_SupplyServiceDoesntExist()
    {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                "not existing supply service",
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        Response<Boolean> res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertTrue(res.hadError());
        savedPreviousStateCheck();
    }

    @Test
    public void FailedToPay_PurchaseServiceFailedToCharge()
    {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.BAD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        Response<Boolean> res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertTrue(res.hadError());
        savedPreviousStateCheck();
    }

    @Test
    public void FailedToPay_SupplyServiceFailedToSupply()
    {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.BAD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        Response<Boolean> res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertTrue(res.hadError());
        savedPreviousStateCheck();
    }

    @Test
    public void FailedToPay_EmptyShoppingCart()
    {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        // purchase the second time - ok
        Response<Boolean> res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertFalse(res.hadError());
        assertTrue(res.getObject());

        // purchase an empty shopping cart - fail
        Response<Boolean> res_empty = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertTrue(res_empty.hadError());
    }

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
