package UnitTests;

import DomainLayer.Response;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OwnerTest extends AbstractTest {

    private User user1, user2;
    private Store store;
    private Response<Boolean> r1;

    public OwnerTest() {
        super();
    }

    @Before
    public void setup() {
        this.user1 = mock(User.class);
        when(user1.getName()).thenReturn("user1");
        when(user1.isSubscribed()).thenReturn(true);
        this.user2 = mock(User.class);
        when(user2.getName()).thenReturn("user2");
        when(user2.isSubscribed()).thenReturn(true);
        store = StoreController.getInstance().createStore(user1, "Store1");

    }

//    @Test
//    public void addOwner() {
//        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addOwner(user2, user2, store.getStoreId()));
//        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addOwner(user1, user1, store.getStoreId()));
//        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addOwner(user1, null, store.getStoreId()));
//        try {
//            StoreController.getInstance().addOwner(user1, user2, store.getStoreId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//        assertTrue(store.isOwner(user1));
//        assertTrue(store.isOwner(user2));
//    }


}
