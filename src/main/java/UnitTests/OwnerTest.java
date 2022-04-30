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

public class OwnerTest extends AbstractTest {

    private User user1, user2;
    private Store store;
    private Response<Boolean> r1;

    public OwnerTest() {
        super();
    }

    @Before
    public void setup() {
        this.user1 = new User(new SubscribedState("user1@gmail.com", "user1", "password"));
        UserController.getInstance().addUser(user1);
        this.user2 = new User(new SubscribedState("user2@gmail.com", "user2", "password"));
        UserController.getInstance().addUser(user2);
        store = StoreController.getInstance().createStore(user1,"Store1");

    }

    @After
    public void clean(){
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("user2");
        UserController.getInstance().removeUser("user1");
    }

    @Test
    public void addOwner(){
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addOwner(user2,user2,store.getStoreId()));
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addOwner(user1,user1,store.getStoreId()));
        assertThrows(NullPointerException.class, () -> StoreController.getInstance().addOwner(user1,null,store.getStoreId()));
        try{
            StoreController.getInstance().addOwner(user1,user2,store.getStoreId());
        }catch (Exception e){
            fail();
        }
        assertTrue(store.isOwner(user1));
        assertTrue(store.isOwner(user2));
    }


}
