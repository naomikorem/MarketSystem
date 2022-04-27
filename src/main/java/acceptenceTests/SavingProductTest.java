package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.ShoppingBasket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SavingProductTest extends AbstractTest {
/*
    private Store s;
    private Item i;

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user1", "password");
        bridge.register("user222@gmail.com", "user2", "password");
        bridge.login("user1", "password");
        bridge.addNewStore("Store1");
        s = bridge.addNewStore("Store1").getObject();
        i = bridge.addItemToStore(s.getStoreId(), "Item1", "Food", 100, 9).getObject();
    }

    @Test
    public void addItemToCartTest() {
        bridge.addItemToCart(s.getStoreId(), i.getId(), 5);
        List<ShoppingBasket> baskets = bridge.getCartBaskets().getObject();
        assertEquals(baskets.size(), 1);
        assertEquals(baskets.get(0).getItemsAndAmounts().size(), 1);

        Set<Map.Entry<Item, Integer>> itemsAmounts = baskets.get(0).getItemsAndAmounts();
        Map.Entry<Item, Integer> itemAmount = itemsAmounts.iterator().next();
        assertEquals();
    }*/
}
