package acceptenceTests;

import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangePremissionsTest extends AbstractTest {

    private User user1, user2, manager;
    private Store store;

    public ChangePremissionsTest() {
        super();
    }

    @Before
    public void before() {
        bridge.enter();
        this.user1 = bridge.register("user123@gmail.com", "user", "first","last","useruser").getObject();
        this.user2 = bridge.register("user2@gmail.com", "user2","first","last", "user2").getObject();
        bridge.login("user", "useruser");
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
        bridge.updateManagerPermissions(store.getStoreId(), user2.getName(), (byte) 0x1);
        Permission perm = bridge.getManagersPermissions(store.getStoreId(), user2.getName()).getObject();
        assertTrue(perm.canAssignManager());
        assertFalse(perm.canChangeItems());
        bridge.updateManagerPermissions(store.getStoreId(), user2.getName(), (byte) 0x2);
        perm = bridge.getManagersPermissions(store.getStoreId(), user2.getName()).getObject();
        assertFalse(perm.canAssignManager());
        assertTrue(perm.canChangeItems());
        bridge.updateManagerPermissions(store.getStoreId(), user2.getName(), (byte) 0x3);
        perm = bridge.getManagersPermissions(store.getStoreId(), user2.getName()).getObject();
        assertTrue(perm.canAssignManager());
        assertTrue(perm.canChangeItems());
        bridge.logout();
    }

}

