package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class StoreInventoryTest extends AbstractTest {

    private Store s;

    public StoreInventoryTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user","first","last", "password");
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);
        s = bridge.addNewStore("store").getObject();
    }

    @After
    public void afterEach() {
        bridge.logout();
    }

    @Test
    public void testAddItemSuccess() {
        Response<Item> i = bridge.addItemToStore(s.getStoreId(), "item", Category.Food, 1, 1);
        assertFalse(i.hadError());
        Response<Map<Item, Integer>> items = bridge.getItems(s.getStoreId());
        assertFalse(items.hadError());
        assertTrue(items.getObject().containsKey(i.getObject()));
    }

    @Test
    public void testAddItemAsManagerSuccess() {
        bridge.addManager("user", s.getStoreId());
        bridge.setManagerPermission("user", s.getStoreId(), Permission.PermissionEnum.canChangeItems.flag);
        bridge.logout();
        bridge.login("user", "password");
        Response<Item> i = bridge.addItemToStore(s.getStoreId(), "item", Category.Food, 1, 1);
        assertFalse(i.hadError());
        Response<Map<Item, Integer>> items = bridge.getItems(s.getStoreId());
        assertFalse(items.hadError());
        assertTrue(items.getObject().containsKey(i.getObject()));
    }

    @Test
    public void testAddItemFail() {
        bridge.logout();
        bridge.login("user", "password");
        Response<Item> i = bridge.addItemToStore(s.getStoreId(), "item", Category.Food, 1, 1);
        assertTrue(i.hadError());
        Response<Map<Item, Integer>> items = bridge.getItems(s.getStoreId());
        assertFalse(items.hadError());
        assertFalse(items.getObject().containsKey(i.getObject()));
    }


    @Test
    public void testAddItemAsManagerFail() {
        bridge.addManager("user", s.getStoreId());
        bridge.logout();
        bridge.login("user", "password");
        Response<Item> i = bridge.addItemToStore(s.getStoreId(), "item", Category.Food, 1, 1);
        assertTrue(i.hadError());
        Response<Map<Item, Integer>> items = bridge.getItems(s.getStoreId());
        assertFalse(items.hadError());
        assertFalse(items.getObject().containsKey(i.getObject()));
    }
}
