package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangesInShoppingCartTest extends AbstractTest{
    private static Store s;
    private static Item i1;
    private static Item i2;
    private static Item i3;

    public ChangesInShoppingCartTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user1","first","last", "password").getObject();
        bridge.login("user1", "password");

        s = bridge.addNewStore("Store1").getObject();
        i1 = bridge.addItemToStore(s.getStoreId(), "Item1", Category.Food, 100, 9).getObject();
        i2 = bridge.addItemToStore(s.getStoreId(), "Item2", Category.Food, 100, 10).getObject();
        i3 = bridge.addItemToStore(s.getStoreId(), "Item3", Category.Food, 100, 10).getObject();
        bridge.logout();
    }

    @After
    public void clean() {

        bridge.removeItemFromStore(s.getStoreId(),i1.getId(),7);
        bridge.removeItemFromStore(s.getStoreId(),i2.getId(),9);
        bridge.removeItemFromStore(s.getStoreId(),i3.getId(),10);
        UserController.getInstance().removeUser("user1");

    }

    @Test
    public void ShoppingCartTest() {
        bridge.login("user1", "password");
        List<Item> l = bridge.getShoppingCartItems().getObject();
        assertTrue(l.isEmpty());
        bridge.addItemToCart(s.getStoreId(),i1.getId(),2);
        l = bridge.getShoppingCartItems().getObject();
        assertFalse(l.isEmpty());
        assertTrue(l.contains(i1));
        assertFalse(l.contains(i2));
        assertFalse(l.contains(i3));
        bridge.addItemToCart(s.getStoreId(),i2.getId(),1);
        l = bridge.getShoppingCartItems().getObject();
        assertTrue(l.contains(i1));
        assertTrue(l.contains(i2));
        assertFalse(l.contains(i3));
        Response<Boolean> res = bridge.removeItemFromCart(s.getStoreId(),i1,1);
        assertFalse(res.hadError());
        l = bridge.getShoppingCartItems().getObject();
        assertTrue(l.contains(i1));
        assertTrue(l.contains(i2));
        res = bridge.removeItemFromCart(s.getStoreId(),i1,1);
        assertFalse(res.hadError());
        l = bridge.getShoppingCartItems().getObject();
        assertFalse(l.contains(i1));
        assertTrue(l.contains(i2));
        bridge.logout();
    }
}
