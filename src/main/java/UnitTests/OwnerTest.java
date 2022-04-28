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

public class OwnerTest extends AbstractTest {

    private User user1, user2, manager;
    private Store store;
    private Response<Boolean> r1,r2;

    public OwnerTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com","user","useruser").getObject();
        this.user2 = bridge.register("user2@gmail.com","userthesecond","user2").getObject();
        this.store = StoreController.getInstance().createStore(user1,"Store1");

    }

    @After
    public void clean(){
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("userthesecond");
        UserController.getInstance().removeUser("user");
        bridge.logout();
    }

    @Test
    public void addOwner(){
        bridge.login(user1.getName(),"useruser");
        r1 = bridge.addOwner("owner",store.getStoreId());
        assertTrue(r1.hadError());
        r1 = bridge.addOwner(user2.getName(),store.getStoreId());
        assertFalse(r1.hadError());
        r1 = bridge.addOwner(user1.getName(),store.getStoreId());
        assertTrue(r1.hadError());
        assertTrue(bridge.removeOwner(user1.getName(),store.getStoreId()).hadError());
        assertFalse(bridge.removeOwner(user2.getName(),store.getStoreId()).hadError());
        bridge.logout();
    }


}
