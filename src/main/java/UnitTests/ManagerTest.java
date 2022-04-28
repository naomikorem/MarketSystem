package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.GuestState;
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
        this.user1 = bridge.register("user123@gmail.com","user","useruser").getObject();
        this.user2 = bridge.register("user2@gmail.com","userthesecond","user2").getObject();
        this.store = StoreController.getInstance().createStore(user1,"Store1");
        store.addOwner(user1.getName(),user2);
        this.manager = bridge.register("manger@gmail.com","manger","manager1").getObject();
        UserController.getInstance().addUser(manager);
    }

    @After
    public void clean(){
        UserController.getInstance().removeUser(this.manager.getName());
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("userthesecond");
        UserController.getInstance().removeUser("user");
        bridge.logout();
    }

    @Test
    public void appointUnregitered(){
        bridge.login(user1.getName(),"useruser");
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
            bridge.login(user1.getName(),"useruser");
            r1 = bridge.addManager(manager.getName(),store.getStoreId());
            bridge.logout();
        });
        Thread t2 = new Thread(() -> {
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login(user2.getName(),"user2");
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
