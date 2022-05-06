package acceptenceTests;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationTest extends AbstractTest{

    private User user1, user2, user3;
    private Store store;

    public NotificationTest() {
        super();
    }

    @Before
    public void before() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com", "user1","first","last", "pass").getObject();
        this.user2 = bridge.register("user2@gmail.com", "user2", "first","last","pass").getObject();
        this.user3 = bridge.register("user3@gmail.com", "user3", "first","last","pass").getObject();
        bridge.login(user1.getName(), "pass");
        this.store = bridge.addNewStore("Store1").getObject();
        bridge.addOwner("user2", store.getStoreId());
        bridge.addManager("user3", store.getStoreId());
        bridge.logout();
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
