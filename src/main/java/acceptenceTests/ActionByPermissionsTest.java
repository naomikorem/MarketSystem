package acceptenceTests;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActionByPermissionsTest extends AbstractTest{

    private User user1, user2, manager;
    private Store store;

    public ActionByPermissionsTest() {
        super();
    }

    @Before
    public void before() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com", "user","first","last", "useruser").getObject();
        this.user2 = bridge.register("user2@gmail.com", "user2","first","last", "user2").getObject();
        bridge.login(user1.getName(), "useruser");
        this.store = bridge.addNewStore("Store1").getObject();
        bridge.addManager("user2", store.getStoreId());
        bridge.logout();
        this.manager = bridge.register("manger@gmail.com", "manger","first","last", "manager1").getObject();
    }

    @After
    public void clean() {
        UserController.getInstance().removeUser(this.manager.getName());
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("user2");
        UserController.getInstance().removeUser("user");
    }

    @Test
    public void ChangePermissionsTest() {
        bridge.login(user1.getName(),"useruser");
        bridge.updateManagerPermissions(store.getStoreId(), user2.getName(), (byte) 0x2);
        bridge.logout();
        bridge.login(user2.getName(),"user2");
        assertTrue(bridge.addManager(manager.getName(), store.getStoreId()).hadError());
        bridge.logout();

        bridge.login(user1.getName(),"useruser");
        bridge.updateManagerPermissions(store.getStoreId(), user2.getName(), (byte) 0x1);
        bridge.logout();
        bridge.login(user2.getName(),"user2");
        assertFalse(bridge.addManager(manager.getName(), store.getStoreId()).hadError());
        bridge.logout();
    }
}
