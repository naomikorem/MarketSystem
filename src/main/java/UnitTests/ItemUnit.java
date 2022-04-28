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
import DomainLayer.Stores.Item;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ItemUnit extends AbstractTest {
    private User user;
    private static Store s;
    private static Item i1;
    private static Item i2;
    private static Item i3;

    public ItemUnit() {
        super();
    }

    @Before
    public void setup() {
        Bridge bridge = new Real();
        bridge.enter();
        bridge.register("user111@gmail.com", "user1", "password");
        bridge.register("user222@gmail.com", "user2", "password");
        bridge.login("user1", "password");
        bridge.addNewStore("Store1");
        s = bridge.addNewStore("Store1").getObject();
        i1 = bridge.addItemToStore(s.getStoreId(), "Item1", "Food", 100, 9).getObject();
        i2 = bridge.addItemToStore(s.getStoreId(), "Item2", "Food", 100, 10).getObject();
        i3 = bridge.addItemToStore(s.getStoreId(), "Item3", "Food", 100, 10).getObject();
        bridge.logout();

    }

    @After
    public void clean() {
        UserController.getInstance().removeUser("user1");
        UserController.getInstance().removeUser("user2");
    }

    @Test
    public void UnitTestItem() {
        assertThrows( NullPointerException.class, ()-> StoreController.getInstance().getItemsWithNameContains("abc"));

    }
}
