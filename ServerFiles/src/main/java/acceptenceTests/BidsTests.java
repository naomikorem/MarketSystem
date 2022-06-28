package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.*;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class BidsTests extends AbstractTest {
    private User user;
    private static Store store;
    private static Item i1;
    private static Item i2;
    private static Item i3;
    private PaymentParamsDTO paymentParamsDTO;
    private SupplyParamsDTO supplyParamsDTO;

    public BidsTests() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user1", "first", "last", "password");
        bridge.login("user1", "password");
        store = bridge.addNewStore("Store1").getObject();
        i1 = bridge.addItemToStore(store.getStoreId(), "Item1", Category.Food, 100, 9).getObject();
        i2 = bridge.addItemToStore(store.getStoreId(), "Item2", Category.Food, 100, 10).getObject();
        i3 = bridge.addItemToStore(store.getStoreId(), "Item3", Category.Food, 100, 10).getObject();
        paymentParamsDTO = new PaymentParamsDTO(
                AbstractProxy.GOOD_STUB_NAME,
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        supplyParamsDTO = new SupplyParamsDTO(
                AbstractProxy.WSEP_PAYMENT,
                "user",
                "user address",
                "bear shava",
                "israel",
                "777777");
    }

    @After
    public void clean() {
        bridge.removeItemFromStore(store.getStoreId(), i1.getId(), 7);
        bridge.removeItemFromStore(store.getStoreId(), i2.getId(), 9);
        bridge.removeItemFromStore(store.getStoreId(), i3.getId(), 10);
        UserController.getInstance().removeUser("user1");
        bridge.logout();
    }

    @Test
    public void testAcceptBids() {
        Collection<Bid> store_bids = bridge.getBids(store.getStoreId()).getObject();
        Collection<Bid> user_bids = bridge.getUserBids().getObject();

        assertTrue(bridge.getBids(store.getStoreId()).getObject().isEmpty());
        assertTrue(bridge.getUserBids().getObject().isEmpty());
        bridge.addBid(store.getStoreId(), 1, i1.getId(), 2);
        bridge.addBid(store.getStoreId(), 3, i2.getId(), 3);

        bridge.approveBid(store.getStoreId(), 1);
        bridge.addBidToCart(1);
        Bid second  = bridge.updateBid(store.getStoreId(), 2, 5).getObject();

        store_bids = bridge.getBids(store.getStoreId()).getObject();
        user_bids = bridge.getUserBids().getObject();

        assertEquals(2, store_bids.size());
        assertEquals(2, user_bids.size());

        Bid first = store_bids.stream().filter(b -> b.getId() == 1).findFirst().get();
        assertTrue(first.getApproved());
        assertTrue(first.isInCart());
        assertTrue(5 == second.getBidPrice());
        Bid finalSecond = second;
        second = user_bids.stream().filter(b -> b.getId() == finalSecond.getId()).findFirst().get();
        assertTrue(5 == second.getBidPrice());


        this.bridge.purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        store_bids = bridge.getBids(store.getStoreId()).getObject();
        user_bids = bridge.getUserBids().getObject();
        assertEquals(1, store_bids.size());
        assertEquals(1, user_bids.size());

        this.bridge.deleteBid(store.getStoreId(),2);
        store_bids = bridge.getBids(store.getStoreId()).getObject();
        user_bids = bridge.getUserBids().getObject();
        assertTrue( store_bids.isEmpty());
        assertTrue( user_bids.isEmpty());

        assertTrue(bridge.getUserBids().getObject().isEmpty());

        Store s = new Store(user, "Use", 3);
        assertNull(StoreController.getInstance().getStore(s.getStoreId()));
    }

    @Test
    public void testNegativeOpenStore() {

    }

}


