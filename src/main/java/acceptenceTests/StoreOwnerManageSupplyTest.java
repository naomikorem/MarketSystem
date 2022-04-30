package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreOwnerManageSupplyTest extends AbstractTest{
    private Response<User> r1, r2;
    private User user;
    private Store store;
    public StoreOwnerManageSupplyTest() {
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
        bridge.login(user.getName(), "password");
        this.store = bridge.addNewStore("Store").getObject();
        bridge.addOwner(user.getName(),store.getStoreId());
    }

    @After
    public void clean() {
        bridge.logout();
        UserController.getInstance().removeUser(user.getName());
    }

    @Test
    public void testAcceptOpenStore() {
        Item i1 = bridge.addItemToStore(store.getStoreId(),"prudect","Food", 100, 9).getObject();
        //isn't its store
        assertTrue(bridge.addItemToStore(store.getStoreId()+1,"prudect1","Food", 100, 9).hadError());
        //back to it's store
        assertFalse(bridge.getItems(store.getStoreId()).getObject().isEmpty());
        assertTrue(bridge.getItems(store.getStoreId()).getObject().containsKey(i1));
        bridge.removeItemFromStore(store.getStoreId(),i1.getId(),9);
        assertTrue(bridge.getItems(store.getStoreId()).getObject().get(i1).equals(0));

    }


}
