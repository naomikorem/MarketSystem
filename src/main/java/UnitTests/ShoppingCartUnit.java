package UnitTests;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import acceptenceTests.Bridge;
import acceptenceTests.Real;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingCartUnit extends AbstractTest {
    private static User u;
    private static Store s;
    private static Item i1;
    private static Item i2;
    private static Item i3;

    public ShoppingCartUnit() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        this.u = bridge.register("user111@gmail.com", "user1", "password").getObject();
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

        bridge.removeItemFromStore(s.getStoreId(),i1.getId(),7);
        bridge.removeItemFromStore(s.getStoreId(),i2.getId(),8);
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
        bridge.purchaseShoppingCart("user1","bear shava", "UPS", "hello");
        l = bridge.getShoppingCartItems().getObject();
        //assertTrue(l.isEmpty());
        bridge.logout();
    }

}
