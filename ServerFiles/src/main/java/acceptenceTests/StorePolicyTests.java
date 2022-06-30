package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.Stores.Store;
import org.junit.Before;
import org.junit.Test;

import java.security.Policy;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class StorePolicyTests extends AbstractTest {

    private Store s;
    private Item i1;
    private Item i2;
    private Item i3;
    private Response<SimplePurchasePolicy> r1, r2;

    public StorePolicyTests() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user", "first", "last", "password");
        bridge.login("user", "password");
        this.s = bridge.addNewStore("store").getObject();
        i1 = bridge.addItemToStore(s.getStoreId(), "item1", Category.Food, 10, 20).getObject();
        i2 = bridge.addItemToStore(s.getStoreId(), "item2", Category.Clothing, 11, 1).getObject();
        i3 = bridge.addItemToStore(s.getStoreId(), "item3", Category.Food, 12, 1).getObject();
    }

    @Test
    public void testStoreGeneralPolicySuccess() {
        int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<SimplePurchasePolicy> p1 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
        assertFalse(p1.hadError());
        Response<Boolean> res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());
        Response<AbstractPurchasePolicy> res = bridge.addItemPredicateToPolicy(s.getStoreId(), p1.getObject().getId(), "AND", i1.getId(), i + 2);
        assertFalse(res.hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());
        Response<SimplePurchasePolicy> p2 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
        assertFalse(p2.hadError());
        assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(), p2.getObject().getId(), "AND", i1.getId(), i - 2).hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertTrue(res_bool.hadError());
    }

    @Test
    public void testOrPolicy() {
        Calendar c = Calendar.getInstance();
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<SimplePurchasePolicy> p1 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
        Response<SimplePurchasePolicy> p2 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
        assertFalse(p1.hadError());
        assertFalse(p2.hadError());
        Response<Boolean> res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());
        Response<AbstractPurchasePolicy> e3 = bridge.addItemPredicateToPolicy(s.getStoreId(), p1.getObject().getId(), "OR", i1.getId(), c.get(Calendar.HOUR_OF_DAY) - 1);
        assertFalse(e3.hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertTrue(res_bool.hadError());
        assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(), p1.getObject().getId(), "OR", i1.getId(), c.get(Calendar.HOUR_OF_DAY) + 2).hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());
    }

    @Test
    public void testLimitItemAmountPolicy() {
        Calendar c = Calendar.getInstance();
        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
        Response<SimplePurchasePolicy> p1 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
        assertFalse(p1.hadError());
        Response<Boolean> res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());

        Response<AbstractPurchasePolicy> e3 = bridge.addItemLimitPredicateToPolicy(s.getStoreId(), p1.getObject().getId(), "OR", i1.getId(), 5, 10);
        assertFalse(e3.hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertTrue(res_bool.hadError());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 4).hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertFalse(res_bool.hadError());
        assertTrue(res_bool.getObject());

        assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 6).hadError());
        res_bool = bridge.getIsLegalToPurchase(s.getStoreId());
        assertTrue(res_bool.hadError());
    }

    @Test
    public void addPoliciesSynchronizedTest() {
        Thread t1 = new Thread(() -> {
            r1 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
            Calendar c = Calendar.getInstance();
            assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(),r1.getObject().getId(),"AND", i1.getId(), c.get(Calendar.HOUR_OF_DAY)-1).hadError());
        });
        Thread t2 = new Thread(() -> {
            r2 = bridge.addPolicy(s.getStoreId(), 24, Calendar.getInstance());
            Calendar c = Calendar.getInstance();
            assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(),r2.getObject().getId(), "AND", i1.getId(), c.get(Calendar.HOUR_OF_DAY)+1).hadError());
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertFalse(r1.hadError() || r2.hadError()); //not both failed

            assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
            Response<Boolean> res = bridge.getIsLegalToPurchase(s.getStoreId());
            assertTrue(res.hadError());

            Response<Boolean> result = bridge.removePolicy(s.getStoreId(), r1.getObject().getId());
            assertFalse(result.hadError());
            assertTrue(result.getObject());
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void addPredicateSynchronizedTest() {
        Response<SimplePurchasePolicy> p = bridge.addPolicy(s.getStoreId(),24, Calendar.getInstance());
        Thread t1 = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(),p.getObject().getId(),"AND", i1.getId(), c.get(Calendar.HOUR_OF_DAY)+1).hadError());
        });
        Thread t2 = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            System.out.println("date "+c);
            assertFalse(bridge.addItemNotAllowedInDatePredicateToPolicy(s.getStoreId(),p.getObject().getId(), "AND", i1.getId(),c).hadError());
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
            Response<Boolean> res = bridge.getIsLegalToPurchase(s.getStoreId());
            assertTrue(res.hadError());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addPredicate2SynchronizedTest() {
        Response<SimplePurchasePolicy> p = bridge.addPolicy(s.getStoreId(),24, Calendar.getInstance());

        Thread t1 = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            assertFalse(bridge.addItemPredicateToPolicy(s.getStoreId(),p.getObject().getId(),"OR", i1.getId(), c.get(Calendar.HOUR_OF_DAY)+1).hadError());
        });
        Thread t2 = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            assertFalse(bridge.addItemNotAllowedInDatePredicateToPolicy(s.getStoreId(),p.getObject().getId(), "OR", i1.getId(),c).hadError());
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertFalse(bridge.addItemToCart(s.getStoreId(), i1.getId(), 1).hadError());
            Response<Boolean> res = bridge.getIsLegalToPurchase(s.getStoreId());
            assertFalse(res.hadError());
            assertTrue(res.getObject());
        } catch (Exception e) {
            fail();
        }
    }
}
