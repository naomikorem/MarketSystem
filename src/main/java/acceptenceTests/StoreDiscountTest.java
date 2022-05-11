package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreDiscountTest extends AbstractTest {

    private Store s;
    private Item i1;
    private Item i2;
    private Item i3;

    public StoreDiscountTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user", "first", "last", "password");
        bridge.login("user", "password");
        this.s = bridge.addNewStore("store").getObject();
        i1 = bridge.addItemToStore(s.getStoreId(), "item1", Category.Food, 10, 1).getObject();
        i2 = bridge.addItemToStore(s.getStoreId(), "item2", Category.Clothing, 11, 1).getObject();
        i3 = bridge.addItemToStore(s.getStoreId(), "item3", Category.Food, 12, 1).getObject();
    }

    @Test
    public void testStoreGeneralDiscountSuccess() {
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        assertFalse(bridge.addDiscount(s.getStoreId(), 0.5).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() / 2, 0.0);

        assertFalse(bridge.addItemToCart(s.getStoreId(), i2.getId(), 1).hadError());
        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() / 2 + i2.getPrice() / 2, 0.0);
    }

    @Test
    public void testCategoryDiscount() {
        Response<AbstractDiscountPolicy> disRes = bridge.addDiscount(s.getStoreId(), 0.5);
        assertFalse(disRes.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() / 2, 0.0);

        assertFalse(bridge.addItemToCart(s.getStoreId(), i2.getId(), 1).hadError());
        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() / 2 + i2.getPrice(), 0.0);
    }

    @Test
    public void testItemDiscount() {
        Response<AbstractDiscountPolicy> disRes = bridge.addDiscount(s.getStoreId(), 0.1);
        assertFalse(disRes.hadError());
        assertFalse(bridge.addItemPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", i2.getId()).hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice(), 0.0);

        assertFalse(bridge.addItemToCart(s.getStoreId(), i2.getId(), 1).hadError());
        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() + i2.getPrice() * 0.9, 0.0);
    }


    @Test
    public void testAndDiscount() {
        Response<AbstractDiscountPolicy> disRes = bridge.addDiscount(s.getStoreId(), 0.6);
        assertFalse(disRes.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        assertFalse(bridge.addItemToCart(s.getStoreId(), i3.getId(), 1).hadError());

        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), (i1.getPrice() + i3.getPrice()) * 0.4, 0.0);

        assertFalse(bridge.addItemPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", i1.getId()).hadError());

        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.4 + i3.getPrice(), 0.0);
    }

    @Test
    public void testOrDiscount() {
        Response<AbstractDiscountPolicy> disRes = bridge.addDiscount(s.getStoreId(), 0.6);
        assertFalse(disRes.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        assertFalse(bridge.addItemToCart(s.getStoreId(), i2.getId(), 1).hadError());

        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.4 + i2.getPrice(), 0.0);

        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "OR", "Clothing").hadError());

        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), (i1.getPrice() + i2.getPrice()) * 0.4, 0.0);
    }


    @Test
    public void testXorDiscount() {
        Response<AbstractDiscountPolicy> disRes = bridge.addDiscount(s.getStoreId(), 0.6);
        assertFalse(disRes.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.4, 0.0);

        assertFalse(bridge.addItemPredicateToDiscount(s.getStoreId(), disRes.getObject().getId(), "XOR", i1.getId()).hadError());
        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice(), 0.0);
    }


    @Test
    public void testMaxDiscount() {
        Response<AbstractDiscountPolicy> disRes1 = bridge.addDiscount(s.getStoreId(), 0.6);
        assertFalse(disRes1.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes1.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.4, 0.0);

        Response<AbstractDiscountPolicy> disRes2 = bridge.addExclusiveDiscount(s.getStoreId(), 0.9);
        assertFalse(disRes2.hadError());
        assertFalse(bridge.addBasketRequirementPredicateToDiscount(s.getStoreId(), disRes2.getObject().getId(), "AND", i1.getPrice()).hadError());

        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.1, 0.00000001);

    }

    @Test
    public void testAddDiscount() {
        Response<AbstractDiscountPolicy> disRes1 = bridge.addDiscount(s.getStoreId(), 0.4);
        assertFalse(disRes1.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes1.getObject().getId(), "AND", "Food").hadError());

        Response<AbstractDiscountPolicy> disRes2 = bridge.addDiscount(s.getStoreId(), 0.5);
        assertFalse(disRes2.hadError());
        assertFalse(bridge.addCategoryPredicateToDiscount(s.getStoreId(), disRes2.getObject().getId(), "AND", "Food").hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<Double> priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), i1.getPrice() * 0.1, 0.00000001);


        assertFalse(bridge.addItemToCart(s.getStoreId(), i2.getId(), 1).hadError());
        assertFalse(bridge.addItemToCart(s.getStoreId(), i3.getId(), 1).hadError());
        priceRes = bridge.getCartPrice();
        assertFalse(priceRes.hadError());
        assertEquals(priceRes.getObject(), (i1.getPrice() + i3.getPrice()) * 0.1 + i2.getPrice(), 0.00000001);
    }


}
