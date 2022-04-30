package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class AppointManagerTest extends AbstractTest {

    private User user1, user2, manager;
    private Store store;
    private Response<Boolean> r1, r2;

    public AppointManagerTest() {
        super();
    }

    @Before
    public void before() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com", "user", "useruser").getObject();
        this.user2 = bridge.register("user2@gmail.com", "user2", "user2").getObject();
        bridge.login("user", "useruser");
        this.store = bridge.addNewStore("Store1").getObject();
        bridge.addOwner("user2", store.getStoreId());
        bridge.logout();
        this.manager = bridge.register("manger@gmail.com", "manger", "manager1").getObject();
    }

    @After
    public void clean() {
        UserController.getInstance().removeUser(this.manager.getName());
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("user2");
        UserController.getInstance().removeUser("user");
        bridge.logout();
    }


    @Test
    public void testAppointManager() {
        Thread t1 = new Thread(() -> {
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login(user1.getName(), "useruser");
            r1 = bridge.addManager(manager.getName(), store.getStoreId());
            bridge.logout();
        });
        Thread t2 = new Thread(() -> {
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login(user2.getName(), "user2");
            r2 = bridge.addManager(manager.getName(), store.getStoreId());
            bridge.logout();
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertTrue(r1.hadError() || r2.hadError());
            assertFalse(r1.hadError() && r2.hadError());
        } catch (Exception e) {
            fail(null);
        }
    }

}
