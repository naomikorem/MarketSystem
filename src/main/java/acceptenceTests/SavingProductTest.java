package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.ShoppingBasket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SavingProductTest extends AbstractTest {
    private static Store s;
    private static Item i1;
    private static Item i2;
    private static Item i3;
    private Response<Item> r1, r2;

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

    @Test
    public void addItemToCartTest() {
        bridge.enter();
        bridge.login("user1", "password");
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 5).hadError());
        List<ShoppingBasket> baskets = bridge.getCartBaskets().getObject();
        assertEquals(baskets.size(), 1);
        assertEquals(baskets.get(0).getItemsAndAmounts().size(), 1);

        Set<Map.Entry<Item, Integer>> itemsAmounts = baskets.get(0).getItemsAndAmounts();
        Map.Entry<Item, Integer> itemAmount = itemsAmounts.iterator().next();
        assertEquals(itemAmount.getKey(), i1);
        assertEquals((int) itemAmount.getValue(), 5);

        assertTrue(bridge.addItemToCart(s.getStoreId(), i1.getId(), 5).hadError());
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 4).hadError());

        List<ShoppingBasket> baskets1 = bridge.getCartBaskets().getObject();
        assertEquals(baskets1.size(), 1);
        assertEquals(baskets1.get(0).getItemsAndAmounts().size(), 1);

        Set<Map.Entry<Item, Integer>> itemsAmounts1 = baskets1.get(0).getItemsAndAmounts();
        Map.Entry<Item, Integer> itemAmount1 = itemsAmounts1.iterator().next();
        assertEquals(itemAmount1.getKey(), i1);
        assertEquals((int) itemAmount1.getValue(), 9);
        bridge.logout();
    }

    @Test
    public void synchronizedSavingProductTestTwoUsers() {
        Thread t1 = new Thread(() -> {
            remock();
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login("user1", "password");
            r1 = bridge.addItemToCart(s.getStoreId(), i2.getId(), 5);
            bridge.logout();
        });
        Thread t2 = new Thread(() -> {
            remock();
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login("user2", "password");
            r2 = bridge.addItemToCart(s.getStoreId(), i2.getId(), 6);
            bridge.logout();
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertTrue(r1.hadError() || r2.hadError()); //one failed to connect
            assertFalse(r1.hadError() && r2.hadError()); //not both failed
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void synchronizedSavingProductTestUserOwner() {
        Thread t1 = new Thread(() -> {
            remock();
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login("user1", "password");
            r1 = bridge.removeItemFromStore(s.getStoreId(), i3.getId(), 5);
            bridge.logout();
        });
        Thread t2 = new Thread(() -> {
            remock();
            Bridge bridge = new Real();
            bridge.enter();
            bridge.login("user2", "password");
            r2 = bridge.addItemToCart(s.getStoreId(), i3.getId(), 6);
            bridge.logout();
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertTrue(r1.hadError() || r2.hadError()); //one failed to connect
            assertFalse(r1.hadError() && r2.hadError()); //not both failed
        } catch (Exception e) {
            fail();
        }
    }
}
