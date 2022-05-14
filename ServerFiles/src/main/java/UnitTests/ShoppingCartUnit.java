package UnitTests;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import acceptenceTests.Bridge;
import acceptenceTests.Real;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static DomainLayer.Stores.Category.Food;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        u = new User(new SubscribedState("user@gmail.com", "user","first","last", "password"));
        s = mock(Store.class);
        when(s.getStoreId()).thenReturn(1);
        i1 = new Item("Item1", Food, 100);
        i2 = new Item("Item2", Food, 10);
        i3 = new Item("Item3", Food, 20);
    }


    @Test
    public void ShoppingCartTest() {
        List<Item> l = u.getShoppingCartItems();
        assertTrue(l.isEmpty());
        s.addItem(i1, 10);
        s.addItem(i2, 10);
        u.addItemToShoppingCart(s.getStoreId(),i1,2);
        l = u.getShoppingCartItems();
        assertFalse(l.isEmpty());
        assertTrue(l.contains(i1));
        assertFalse(l.contains(i2));
        assertFalse(l.contains(i3));
        u.addItemToShoppingCart(s.getStoreId(),i2,5);
        l = u.getShoppingCartItems();
        assertTrue(l.contains(i1));
        assertTrue(l.contains(i2));
        assertFalse(l.contains(i3));
        u.emptyShoppingCart();
        l = u.getShoppingCartItems();
        assertTrue(l.isEmpty());
    }

}
