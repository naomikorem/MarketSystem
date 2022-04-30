package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import acceptenceTests.Bridge;
import acceptenceTests.Real;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class ManagerTest extends AbstractTest {
    private User user1, user2, manager;
    private Store store;
    private Response<Boolean> r1,r2;

    public ManagerTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com","user","pass").getObject();
        this.user2 = bridge.register("user2@gmail.com","user2","pass").getObject();
        bridge.login("user", "pass");
        store = bridge.addNewStore("Store1").getObject();
        bridge.addOwner("user2", store.getStoreId());
        bridge.logout();
        this.manager = bridge.register("manger@gmail.com","manager","manager1").getObject();
    }

    @After
    public void clean(){
        UserController.getInstance().removeUser(this.manager.getName());
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("user2");
        UserController.getInstance().removeUser("user");
        bridge.logout();
    }

    @Test
    public void appointUnregitered(){
        bridge.login(user1.getName(),"pass");
        r1 = bridge.addManager("u1", store.getStoreId());
        assertTrue(r1.hadError());
        r2 = bridge.addManager(manager.getName(),store.getStoreId());
        assertFalse(r2.hadError());
        bridge.logout();
    }

    @Test
    public void testAppointManager() {
        Thread t1 = new Thread(() -> {
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login(user1.getName(),"pass");
            r1 = bridge.addManager(manager.getName(),store.getStoreId());
            bridge.logout();
        });
        Thread t2 = new Thread(() -> {
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login(user2.getName(),"pass");
            r2 = bridge.addManager(manager.getName(),store.getStoreId());
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
            fail((String)null);
        }
    }


}
