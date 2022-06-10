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

import static org.junit.jupiter.api.Assertions.*;

public class RatingTest  extends AbstractTest {
    private static Store s;
    private static Item i1;

    public RatingTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user1", "first", "last", "password").getObject();
        bridge.login("user1", "password");

        s = bridge.addNewStore("Store1").getObject();
        i1 = bridge.addItemToStore(s.getStoreId(), "Item1", Category.Food, 100, 9).getObject();
        bridge.logout();

    }

    @After
    public void clean() {
        bridge.removeItemFromStore(s.getStoreId(),i1.getId(),7);
        UserController.getInstance().removeUser("user1");
    }

    @Test
    public void RateItemsTest() {
        bridge.login("user1", "password");
        List<Item> l = bridge.getShoppingCartItems().getObject();
        assertTrue(l.isEmpty());
        bridge.addItemToCart(s.getStoreId(),i1.getId(),2);
        l = bridge.getShoppingCartItems().getObject();
        Response<Double> rate = bridge.getItemRating(s.getStoreId(),i1.getId());
        assertFalse(rate.hadError());
        assertEquals(rate.getObject(),0);
        Response<Boolean> res = bridge.setItemRating(s.getStoreId(),i1.getId(),6);
        assertFalse(res.hadError());
        assertFalse(res.getObject());
        res = bridge.setItemRating(s.getStoreId(),i1.getId(),-1);
        assertFalse(res.hadError());
        assertFalse(res.getObject());
        res = bridge.setItemRating(s.getStoreId(),i1.getId(),4);
        assertFalse(res.hadError());
        assertTrue(res.getObject());
        rate = bridge.getItemRating(s.getStoreId(),i1.getId());
        assertFalse(rate.hadError());
        assertNotEquals(rate.getObject(),0);
        bridge.logout();
    }
}
