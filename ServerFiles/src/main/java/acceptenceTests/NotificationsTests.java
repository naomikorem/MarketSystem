package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.Notification;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class NotificationsTests extends AbstractTest
{
    private final String username1, store1_owner_username, store2_owner_username;
    private int store1_id, store2_id;
    private int item1_id, item2_id, item3_id, item4_id;
    private User user1;
    private Store store;
    private PaymentParamsDTO paymentParamsDTO;
    private SupplyParamsDTO supplyParamsDTO;

    public NotificationsTests()
    {
        super();
        this.username1 = "regularUser1";
        this.store1_owner_username = "store1owner";
        this.store2_owner_username = "store2owner";
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

        //bridge.initializeMarket();
        bridge.enter();

        addStubs();

        bridge.register("user111@gmail.com", username1, "first","last","password");
        bridge.register("user222@gmail.com", store1_owner_username, "first","last","password");
        bridge.register("user223@gmail.com", store2_owner_username, "first","last","password");

        assertFalse(bridge.login(store1_owner_username, "password").hadError());
        this.store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        Item item1 = bridge.addItemToStore(store1_id, "Item1", Category.Food, 10, 100000).getObject();
        Item item2 = bridge.addItemToStore(store1_id, "Item2", Category.Food, 8, 60000).getObject();
        bridge.logout();

        assertFalse(bridge.login(store2_owner_username, "password").hadError());
        this.store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        Item item3 = bridge.addItemToStore(store2_id, "Item3", Category.Food, 10, 100000).getObject();
        Item item4 = bridge.addItemToStore(store2_id, "Item4", Category.Food, 8, 600000).getObject();
        bridge.logout();

        this.item1_id = item1.getId();
        this.item2_id = item2.getId();
        this.item3_id = item3.getId();
        this.item4_id = item4.getId();

        this.user1 = bridge.register("user123@gmail.com", "user1","first","last", "pass").getObject();
        bridge.register("user2@gmail.com", "user2", "first", "last", "pass").getObject();
        bridge.register("user3@gmail.com", "user3", "first", "last", "pass").getObject();
        bridge.login(user1.getName(), "pass");
        this.store = bridge.addNewStore("Store3").getObject();
        bridge.addOwner("user2", store.getStoreId());
        bridge.addManager("user3", store.getStoreId());
        bridge.logout();
    }

    @Test
    public void notifyStoresOwnersAfterUserPurchaseTest()
    {
        this.bridge.login(username1, "password");
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);
        this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        this.bridge.logout();
        this.bridge.login(store1_owner_username, "password");
        Response<List<String>> owners_store_1_res = bridge.getStoreOwners(store1_id);
        assertFalse(owners_store_1_res.hadError());
        Response<List<String>> owners_store_2_res = bridge.getStoreOwners(store2_id);
        assertFalse(owners_store_2_res.hadError());

        List<String> owners1 = owners_store_1_res.getObject();
        List<String> owners2 = owners_store_2_res.getObject();

        assertEquals(1, owners1.size());
        assertEquals(1, owners2.size());

        assertTrue(owners1.contains(store1_owner_username));
        assertTrue(owners2.contains(store2_owner_username));

        Response<List<INotification>> owner1_notification_res = bridge.getUserNotifications();
        assertFalse(owner1_notification_res.hadError());
        List<INotification> notification1 = owner1_notification_res.getObject();
        assertEquals(1, notification1.size());
        this.bridge.logout();

        this.bridge.login(store2_owner_username, "password");
        Response<List<INotification>> owner2_notification_res = bridge.getUserNotifications();
        assertFalse(owner2_notification_res.hadError());
        List<INotification> notification2 = owner2_notification_res.getObject();
        assertEquals(1, notification2.size());
        this.bridge.logout();
    }

    @Test
    public void notifyStoresOwnersAfterGuestPurchaseTest()
    {
        this.bridge.addItemToCart(store1_id, item1_id, 1);
        this.bridge.addItemToCart(store1_id, item2_id, 2);
        this.bridge.addItemToCart(store2_id, item3_id, 1);
        this.bridge.addItemToCart(store2_id, item4_id, 2);
        this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);

        // check that store 1 owner received notification
        this.bridge.login(store1_owner_username, "password");
        Response<List<INotification>> owner1_notification_res = bridge.getUserNotifications();
        assertFalse(owner1_notification_res.hadError());
        assertEquals(1, owner1_notification_res.getObject().size());
        assertTrue(owner1_notification_res.getObject().iterator().next().getMessage().contains(HistoryController.GUEST_DEFAULT_NAME));
        this.bridge.logout();

        // check that store  owner received notification
        this.bridge.login(store2_owner_username, "password");
        Response<List<INotification>> owner2_notification_res = bridge.getUserNotifications();
        assertFalse(owner2_notification_res.hadError());
        List<INotification> notification2 = owner2_notification_res.getObject();
        assertEquals(1, notification2.size());
        assertTrue(notification2.iterator().next().getMessage().contains(HistoryController.GUEST_DEFAULT_NAME));
        this.bridge.logout();
    }

    @Test
    public void testRemovedNotification() {
        bridge.login(user1.getName(),"pass");
        bridge.removeOwner("user2", store.getStoreId());
        bridge.removeManager("user3", store.getStoreId());
        bridge.logout();

        bridge.login("user2", "pass");
        Response<List<INotification>> user2_notifications = bridge.getUserNotifications();
        assertFalse(user2_notifications.hadError());
        assertEquals(user2_notifications.getObject().size(), 1);
        assertTrue(user2_notifications.getObject().get(0).getMessage().contains("You were removed as an owner of store"));
        bridge.logout();

        bridge.login("user3", "pass");
        Response<List<INotification>> user3_notifications = bridge.getUserNotifications();
        assertFalse(user3_notifications.hadError());
        assertEquals(user3_notifications.getObject().size(), 1);
        assertTrue(user3_notifications.getObject().get(0).getMessage().contains("You were removed as a manager of store"));
        bridge.logout();
    }

    @Test
    public void testClosedNotification() {
        bridge.login(user1.getName(),"pass");
        bridge.closeStore(store.getStoreId());
        bridge.logout();

        bridge.login("user2", "pass");
        Response<List<INotification>> user2_notifications = bridge.getUserNotifications();
        assertFalse(user2_notifications.hadError());
        assertEquals(user2_notifications.getObject().size(), 1);
        assertTrue(user2_notifications.getObject().get(0).getMessage().contains("that is owned by you was shut down"));
        bridge.logout();

        bridge.login("user3", "pass");
        Response<List<INotification>> user3_notifications = bridge.getUserNotifications();
        assertFalse(user3_notifications.hadError());
        assertEquals(user3_notifications.getObject().size(), 1);
        assertTrue(user3_notifications.getObject().get(0).getMessage().contains("that is managed by you was shut down"));
        bridge.logout();
    }

    /*@Test
    public void synchronizedNotificationTest() {
        for(int i = 1; i < 100; i++)
        {
            Thread t1 = new Thread(() -> {
                Bridge bridge_user = new Real();
                bridge_user.enter();
                bridge_user.login(username1, "password");
                bridge_user.addItemToCart(store1_id, item1_id, 1);
                bridge_user.addItemToCart(store1_id, item2_id, 2);
                this.user_purchase_res = bridge_user.purchaseShoppingCart("ashdod", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);
                bridge_user.logout();
            });
            Thread t2 = new Thread(() -> {
                Bridge bridge_owner = new Real();
                bridge_owner.enter();
                bridge_owner.login(store1_owner_username, "password");
                assertFalse(bridge_owner.addItemToCart(store2_id, item3_id, 1).hadError());
                assertFalse(bridge_owner.addItemToCart(store2_id, item4_id, 2).hadError());
                this.store1_owner_purchase_res = bridge_owner.purchaseShoppingCart("ashdod", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);
                bridge_owner.logout();
            });
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
                if(this.user_purchase_res.hadError())
                    System.out.println("User regular fail");
                else if(this.store1_owner_purchase_res.hadError())
                    System.out.println("Store 1 owner fail");

                assertFalse(this.user_purchase_res.hadError() || this.store1_owner_purchase_res.hadError());

                //check that the store 1 owner received notifications
                this.bridge.login(this.store1_owner_username, "password");
                Response<List<INotification>> store1_owner_notification_res = this.bridge.getUserNotifications(); // supposed to be one notification from regular user per round
                Response<List<INotification>> store1_owner_realtime_notification_res = this.bridge.getUserRealTimeNotifications(); // supposed to be one notification from regular user per round
                Response<History> store1_history_res = this.bridge.getStoreHistory(store1_id); // supposed to be 2 history items from user purchase
                this.bridge.logout();

                 assertTrue(checkAmountOfGoodListAndBadList(i, new Date(), store1_owner_notification_res, store1_owner_realtime_notification_res));//

                // check that the purchase history of user 1 added to store 1
                assertFalse(store1_history_res.hadError());
                assertEquals(2*i, store1_history_res.getObject().getHistoryItems().size());

                // receive information about store 2
                this.bridge.login(this.store2_owner_username, "password");
                Response<List<INotification>> store2_owner_notification_res = this.bridge.getUserNotifications(); // supposed to be 1 from store owner 1
                Response<History> store2_history_res = this.bridge.getStoreHistory(store2_id); // supposed to be 2 from store owner 1 purchase
                this.bridge.logout();
                // check that the store 2 owner received notifications
                assertFalse(store2_owner_notification_res.hadError());
                assertEquals(i, store2_owner_notification_res.getObject().size());

                // check that the purchase history of store owner 1 added to store 2
                assertFalse(store2_history_res.hadError());
                assertEquals(2*i, store2_history_res.getObject().getHistoryItems().size());
            } catch (Exception e) {
                fail(null);
            }
        }
    }*/

    private boolean checkAmountOfGoodListAndBadList(int expected, Date timestamp, Response<List<INotification>> response_notification, Response<List<INotification>> response_realtime_notification)
    {
        if(response_notification.hadError())
            return response_realtime_notification.getObject().size() == expected;
        else if(response_realtime_notification.hadError())
            return response_notification.getObject().size() == expected;
        else
            return (response_notification.getObject().size() + response_realtime_notification.getObject().size()) == expected;
    }
}
