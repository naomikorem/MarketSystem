package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PurchaseHistoryTests extends AbstractTest
{
    private final String regular_username1, regular_username2, store1_owner_username, store2_owner_username, admin_username;
    private int store1_id, store2_id;
    private Item item3, item4;
    private int item3_id, item4_id;
    private Set<Item> originals_items_store1_user1, originals_items_store2_user2;
    private PaymentParamsDTO paymentParamsDTO;
    private SupplyParamsDTO supplyParamsDTO;

    public PurchaseHistoryTests()
    {
        super();
        this.regular_username1 = "regularUser1";
        this.regular_username2 = "regularUser2";
        this.store1_owner_username = "store1owner";
        this.store2_owner_username = "store2owner";
        this.admin_username = "admin";
        this.store1_id = 1;
        this.store2_id = 2;
    }

    @Before
    public void setup()
    {
        paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");

        bridge.enter();
        addStubs();
        bridge.register("user111@gmail.com", regular_username1,"first","last", "password");
        bridge.register("user112@gmail.com", regular_username2,"first","last", "password");
        bridge.register("user222@gmail.com", store1_owner_username, "first","last","password");
        bridge.register("user223@gmail.com", store2_owner_username, "first","last","password");

        assertFalse(bridge.login(store1_owner_username, "password").hadError());
        this.store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        Item item1 = bridge.addItemToStore(store1_id, "Item1", Category.Food, 10, 10).getObject();
        int item1_id = item1.getId();
        Item item2 = bridge.addItemToStore(store1_id, "Item2", Category.Food, 8, 6).getObject();
        int item2_id = item2.getId();
        bridge.logout();

        assertFalse(bridge.login(store2_owner_username, "password").hadError());
        this.store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        this.item3 = bridge.addItemToStore(store2_id, "Item3", Category.Food, 10, 10).getObject();
        this.item3_id = this.item3.getId();
        this.item4 = bridge.addItemToStore(store2_id, "Item4", Category.Food, 8, 6).getObject();
        this.item4_id = this.item4.getId();
        bridge.logout();

        this.bridge.login(regular_username1, "password");
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        this.bridge.logout();

        this.bridge.login(regular_username2, "password");
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);
        this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        this.bridge.logout();

        this.originals_items_store1_user1 = new HashSet<>();
        originals_items_store1_user1.add(item1);
        originals_items_store1_user1.add(item2);

        this.originals_items_store2_user2 = new HashSet<>();
        originals_items_store2_user2.add(item3);
        originals_items_store2_user2.add(item4);
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
        assertTrue(compareHistoryItemsToRegularItems(items, originals_items_store1_user1));

        this.bridge.logout();
    }

    @Test
    public void AdminReceiveUsersPurchaseHistory()
    {
        this.bridge.login(admin_username, "admin");

        Response<History> res = this.bridge.getPurchaseHistory(regular_username1);
        assertFalse(res.hadError());

        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertTrue(compareHistoryItemsToRegularItems(items, originals_items_store1_user1));

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
        assertTrue(compareHistoryItemsToRegularItems(items, originals_items_store1_user1));

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
        assertTrue(compareHistoryItemsToRegularItems(items1, originals_items_store1_user1));

        Response<History> res2 = this.bridge.getStoreHistory(store2_id);
        assertFalse(res2.hadError());
        Set<ItemHistory> items2 = res2.getObject().getHistoryItems();
        assertTrue(compareHistoryItemsToRegularItems(items2, originals_items_store2_user2));

        this.bridge.logout();
    }

    @Test
    public void successfulPurchaseTest()
    {
        this.bridge.login(regular_username1, "password");
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);

        Set<Item> originals_items_store2_user1 = new HashSet<>();
        originals_items_store2_user1.add(item3);
        originals_items_store2_user1.add(item4);

        // check that the payment and supply confirmed.
        Response<Boolean> purchase_res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertFalse(purchase_res.hadError());
        assertTrue(purchase_res.getObject());

        // check that the user's shopping cart is now empty
        Response<List<Item>> shopping_cart_left_items_res = bridge.getShoppingCartItems();
        assertFalse(shopping_cart_left_items_res.hadError());
        assertTrue(shopping_cart_left_items_res.getObject().isEmpty());

        // check that the personal history contains all the items that the user bought
        Set<Item> copy = new HashSet<>(originals_items_store1_user1);
        copy.addAll(originals_items_store2_user1);
        Response<History> res = this.bridge.getPurchaseHistory();
        this.bridge.logout();
        assertFalse(res.hadError());
        Set<ItemHistory> items = res.getObject().getHistoryItems();
        assertTrue(compareHistoryItemsToRegularItems(items, copy));

        // check that store 1 contains item1,2 in it's purchase history
        this.bridge.login(store1_owner_username, "password");
        Response<History> store1_history_res = this.bridge.getStoreHistory(store1_id);
        assertFalse(store1_history_res.hadError());
        assertTrue(compareHistoryItemsToRegularItems(store1_history_res.getObject().getHistoryItems(), originals_items_store1_user1));
        this.bridge.logout();

        // check that store 2 contains item3,4 of user 1 in it's purchase history
        this.bridge.login(store2_owner_username, "password");
        Response<History> store2_history_res = this.bridge.getStoreHistory(store2_id);
        assertFalse(store2_history_res.hadError());
        assertTrue(compareHistoryItemsToRegularItems(store2_history_res.getObject().getHistoryItems().stream().filter(item -> item.username.equals(regular_username1)).collect(Collectors.toSet()),
                originals_items_store2_user1));
        this.bridge.logout();
    }

    @Test
    public void guestPurchaseShoppingCart()
    {
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);

        // check that the payment and supply confirmed.
        Response<Boolean> purchase_res = this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        assertFalse(purchase_res.hadError());
        assertTrue(purchase_res.getObject());

        // check that the user's shopping cart is now empty
        Response<List<Item>> shopping_cart_left_items_res = bridge.getShoppingCartItems();
        assertFalse(shopping_cart_left_items_res.hadError());
        assertTrue(shopping_cart_left_items_res.getObject().isEmpty());

        // check that the guest user doesn't have personal purchase history
        assertTrue(this.bridge.getPurchaseHistory().hadError());

        // check that admin user can't receive history of guest
        assertFalse(this.bridge.login(admin_username, "admin").hadError());
        assertTrue(this.bridge.getPurchaseHistory(HistoryController.GUEST_DEFAULT_NAME).hadError());
        assertFalse(this.bridge.logout().hadError());

        // check that the store history contains all the items that the user bought
        assertFalse(this.bridge.login(store2_owner_username, "password").hadError());
        Response<History> res = this.bridge.getStoreHistory(store2_id);
        this.bridge.logout();
        assertFalse(res.hadError());
        Set<ItemHistory> items = res.getObject().getHistoryItems();
        items = items.stream().filter(item -> item.username.equals(HistoryController.GUEST_DEFAULT_NAME)).collect(Collectors.toSet());
        assertTrue(compareHistoryItemsToRegularItems(items, originals_items_store2_user2));
    }
}
