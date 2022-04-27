package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.Assert.*;

public class OpenStoreTest extends AbstractTest {
    private Response<User> r1, r2;
    private User user;

    public OpenStoreTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        if (UserController.getInstance().isExist("user")) {
            UserController.getInstance().removeUser("user");
        }
        Response<User> u = bridge.register("user1@gmail.com", "user", "password");
        this.user = u.getObject();
    }

    @AfterAll
    public void clean(){
        if (UserController.getInstance().isExist("user")) {
            UserController.getInstance().removeUser("user");
        }
    }

    @Test
    public void testAcceptOpenStore() {
        Store store = StoreController.getInstance().createStore(user, "Mystore");
        assertTrue(StoreController.getInstance().getStore(store.getStoreId()).equals(store));
        assertTrue(StoreController.getInstance().getAllStores().contains(store));
        assertTrue(StoreController.getInstance().isShopOwner(store, "user"));
        Store s = new Store("User", "Use", 3);
        assertNull(StoreController.getInstance().getStore(s.getStoreId()));

    }

    @Test
    public void testNegativeOpenStore() {
        Store store = new Store("User", "Use", 3);
        assertFalse(StoreController.getInstance().getAllStores().contains(store));
    }


}
