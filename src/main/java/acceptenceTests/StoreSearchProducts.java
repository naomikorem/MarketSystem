package acceptenceTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StoreSearchProducts extends AbstractTest {

    private Store store;
    private static Item i1;
    private static Item i2;
    private static Item i3;
    private User user;
    private static String storeName;
    private static boolean isInit = false;

    public StoreSearchProducts() {
        super();
    }

    @Before
    public void setup() {
        if (!isInit) {
            isInit = true;
            bridge.enter();
            this.user = bridge.register("user111@gmail.com", "user", "first","last","password").getObject();
            bridge.login("user", "password");
            this.storeName = "MyStore";
            this.store = bridge.addNewStore(storeName).getObject();
            this.i1 = bridge.addItemToStore(store.getStoreId(), "Item1", Category.Food, 100, 9).getObject();
            this.i2 = bridge.addItemToStore(store.getStoreId(), "Item2", Category.Food, 100, 10).getObject();
            this.i3 = bridge.addItemToStore(store.getStoreId(), "Item3", Category.Clothing, 100, 10).getObject();
        }
    }

    @After
    public void clean(){
        bridge.removeItemFromStore(store.getStoreId(),i1.getId(),9);
        bridge.removeItemFromStore(store.getStoreId(),i2.getId(),10);
        bridge.removeItemFromStore(store.getStoreId(),i3.getId(),10);
        UserController.getInstance().removeUser("user");
    }

    @Test
    public void testStoreInformationFailure() {
        assertTrue(store.getItemsWithCategory(Category.Food).contains(i1));
        assertTrue(store.getItemsWithCategory(Category.Food).contains(i2));
        assertFalse(store.getItemsWithCategory(Category.Food).contains(i3));
        assertTrue(store.getItemsWithCategory(Category.Toys).isEmpty());
        Set<Item> items = store.getItemsWithNameContains("Item");
        assertTrue(items.contains(i1));
        assertTrue(items.contains(i2));
        assertTrue(items.contains(i3));
        assertFalse(store.getItemsWithNameContains("Item1").isEmpty());
    }


}
