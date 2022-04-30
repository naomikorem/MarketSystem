package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.Notification;
import DomainLayer.Users.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class NotificationsTests extends AbstractTest
{
    private final String username1, store1_owner_username, store2_owner_username;
    private int store1_id, store2_id;
    private Item item1, item2, item3, item4;
    private int item1_id, item2_id, item3_id, item4_id;
    private User user1, user2, user3;
    private Store store;

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
        bridge.initializeMarket();
        bridge.enter();
        bridge.register("user111@gmail.com", username1, "password");
        bridge.register("user222@gmail.com", store1_owner_username, "password");
        bridge.register("user223@gmail.com", store2_owner_username, "password");

        assertFalse(bridge.login(store1_owner_username, "password").hadError());
        this.store1_id = bridge.addNewStore("Store1").getObject().getStoreId();
        this.item1 = bridge.addItemToStore(store1_id, "Item1", "Food", 10, 10).getObject();
        this.item2 = bridge.addItemToStore(store1_id, "Item2", "Food", 8, 6).getObject();
        bridge.logout();

        assertFalse(bridge.login(store2_owner_username, "password").hadError());
        this.store2_id = bridge.addNewStore("Store2").getObject().getStoreId();
        this.item3 = bridge.addItemToStore(store2_id, "Item3", "Food", 10, 10).getObject();
        this.item4 = bridge.addItemToStore(store2_id, "Item4", "Food", 8, 6).getObject();
        bridge.logout();

        this.item1_id = item1.getId();
        this.item2_id = item2.getId();
        this.item3_id = item3.getId();
        this.item4_id = item4.getId();

        this.user1 = bridge.register("user123@gmail.com", "user1", "pass").getObject();
        this.user2 = bridge.register("user2@gmail.com", "user2", "pass").getObject();
        this.user3 = bridge.register("user3@gmail.com", "user3", "pass").getObject();
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
        this.bridge.purchaseShoppingCart("ashdod", "stub", "stub");
        this.bridge.logout();

        this.bridge.login(store1_owner_username, "password");
        Response<List<String>> owners_store_1_res = bridge.getStoreOwners(store1_id);
        assertFalse(owners_store_1_res.hadError());
        Response<List<String>> owners_store_2_res = bridge.getStoreOwners(store2_id);
        assertFalse(owners_store_2_res.hadError());

        List<String> owners1 = owners_store_1_res.getObject();
        List<String> owners2 = owners_store_2_res.getObject();

        assertTrue(owners1.size() == 1);
        assertTrue(owners2.size() == 1);

        assertTrue(owners1.contains(store1_owner_username));
        assertTrue(owners2.contains(store2_owner_username));

        Response<List<INotification>> owner1_notification_res = bridge.getUserNotifications();
        assertFalse(owner1_notification_res.hadError());
        List<INotification> notification1 = owner1_notification_res.getObject();
        assertTrue(notification1.size() == 1);
        this.bridge.logout();

        this.bridge.login(store2_owner_username, "password");
        Response<List<INotification>> owner2_notification_res = bridge.getUserNotifications();
        assertFalse(owner2_notification_res.hadError());
        List<INotification> notification2 = owner2_notification_res.getObject();
        assertTrue(notification2.size() == 1);
        this.bridge.logout();
    }

    @Test
    public void testRemovedNotification() {
        bridge.login(user1.getName(),"pass");
        bridge.removeOwner("user2", store.getStoreId());
        bridge.removeManager("user3", store.getStoreId());
        bridge.logout();

        bridge.login("user2", "pass");
        assertFalse(bridge.getUserNotifications().hadError());
        assertEquals(bridge.getUserNotifications().getObject().size(), 1);
        assertEquals(bridge.getUserNotifications().getObject().get(0).getMessage(), String.format("You were removed as an owner of store %s", store.getStoreId()));
        bridge.logout();

        bridge.login("user3", "pass");
        assertFalse(bridge.getUserNotifications().hadError());
        assertEquals(bridge.getUserNotifications().getObject().size(), 1);
        assertEquals(bridge.getUserNotifications().getObject().get(0).getMessage(), String.format("You were removed as a manager of store %s", store.getStoreId()));
        bridge.logout();
    }

    @Test
    public void testClosedNotification() {
        bridge.login(user1.getName(),"pass");
        bridge.closeStore(store.getStoreId());
        bridge.logout();

        bridge.login("user2", "pass");
        assertFalse(bridge.getUserNotifications().hadError());
        assertEquals(bridge.getUserNotifications().getObject().size(), 1);
        assertEquals(bridge.getUserNotifications().getObject().get(0).getMessage(), String.format("The store %s that is owned by you was shut down", store.getStoreId()));
        bridge.logout();

        bridge.login("user3", "pass");
        assertFalse(bridge.getUserNotifications().hadError());
        assertEquals(bridge.getUserNotifications().getObject().size(), 1);
        assertEquals(bridge.getUserNotifications().getObject().get(0).getMessage(), String.format("The store %s that is managed by you was shut down", store.getStoreId()));
        bridge.logout();
    }

}
