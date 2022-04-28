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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static DomainLayer.Stores.Category.Clothing;
import static DomainLayer.Stores.Category.Food;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ItemUnit extends AbstractTest {

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

        bridge.removeItemFromStore(s.getStoreId(),i1.getId(),9);
        bridge.removeItemFromStore(s.getStoreId(),i2.getId(),10);
        bridge.removeItemFromStore(s.getStoreId(),i3.getId(),10);
        UserController.getInstance().removeUser("user1");
        UserController.getInstance().removeUser("user2");

    }

    @Test
    public void UnitTestItem() {
         HashSet<Item> o = (HashSet<Item>) StoreController.getInstance().getItemsWithNameContains("Item");
         assertTrue(o.contains(i1));
         assertTrue(o.contains(i2));
         assertTrue(o.contains(i3));
         Item i4 = new Item("Item4", Clothing, 3.4);
         assertFalse(o.contains(i4));
         this.i1.setPrice(5.0);
         assertTrue(i1.getPrice()==5.0);
         assertThrows(IllegalArgumentException.class, () -> i1.setPrice(-2));
         i1.setPrice(0);
         assertTrue(i1.getPrice()==0);
         assertTrue(i1.getCategory().equals(Food));
         i1.setCategory(Clothing);
         assertFalse(i1.getCategory().equals(Food));
         assertTrue(i1.getCategory().equals(Clothing));
         i1.setProductName("Item11");
         assertFalse(i1.getProductName().equals("Item1"));
         assertTrue(i1.getProductName().equals("Item11"));

    }
}
