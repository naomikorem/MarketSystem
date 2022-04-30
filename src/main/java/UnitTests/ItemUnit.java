package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
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
import DomainLayer.Stores.Item;
import org.powermock.reflect.Whitebox;

import java.util.*;
import java.util.function.BooleanSupplier;

import static DomainLayer.Stores.Category.Clothing;
import static DomainLayer.Stores.Category.Food;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemUnit extends AbstractTest {

    private Store s;
    private StoreController sc;
    private Item i1;
    private Item i2;
    private Item i3;

    public ItemUnit() {
        super();
    }

    @Before
    public void setup() {
        User u = new User(new SubscribedState("email@gmail.com", "user", "pass"));
        s = new Store(u, "Store1", 1);
        sc = new StoreController();
        Whitebox.setInternalState(sc, "stores", Map.of(1, s));
        i1 = new Item("Item1", Food, 100);
        i2 = new Item("Item2", Food, 100);
        i3 = new Item("Item3", Food, 100);
        Whitebox.setInternalState(s, "items", Map.of(i1, 9, i2, 10, i3, 10));
    }

    @Test
    public void UnitTestItem() {
        System.out.println(sc.getAllStores());
        HashSet<Item> o = (HashSet<Item>) sc.getItemsWithNameContains("Item");
        assertTrue(o.contains(i1));
        assertTrue(o.contains(i2));
        assertTrue(o.contains(i3));
        Item i4 = new Item("Item4", Clothing, 3.4);
        assertFalse(o.contains(i4));
        i1.setPrice(5.0);
        assertEquals(5.0, i1.getPrice(), 0.0);
        assertThrows(IllegalArgumentException.class, () -> i1.setPrice(-2));
        i1.setPrice(0);
        assertEquals(0, i1.getPrice(), 0.0);
        assertEquals(i1.getCategory(), Food);
        i1.setCategory(Clothing);
        assertNotEquals(i1.getCategory(), Food);
        assertEquals(i1.getCategory(), Clothing);
        i1.setProductName("Item11");
        assertNotEquals("Item1", i1.getProductName());
        assertEquals("Item11", i1.getProductName());

    }
}
