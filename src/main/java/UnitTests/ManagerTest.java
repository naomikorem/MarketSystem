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
import static org.junit.Assert.fail;

public class ManagerTest extends AbstractTest {
    private User user1, user2, manager;
    private Store store;
    private Response<Boolean> r1,r2;

    public ManagerTest() {
        super();
    }

    @Before
    public void setup() {
        this.user1 = new User(new SubscribedState("user1@gmail.com", "user1", "password"));
        UserController.getInstance().addUser(user1);
        this.user2 = new User(new SubscribedState("user2@gmail.com", "user2", "password"));
        UserController.getInstance().addUser(user2);
        store = StoreController.getInstance().createStore(user1,"Store1");

        this.manager = new User(new SubscribedState("userM@gmail.com", "manager", "password"));
        UserController.getInstance().addUser(manager);
    }

    @After
    public void clean(){
        UserController.getInstance().removeUser(this.manager.getName());
        StoreController.getInstance().removeStore(store);
        UserController.getInstance().removeUser("user2");
        UserController.getInstance().removeUser("user1");

    }

    @Test
    public void appointUnregitered(){
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(user2,user2,store.getStoreId()));
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(user1,user1,store.getStoreId()));
        assertThrows(NullPointerException.class, () -> StoreController.getInstance().addManager(user1,null,store.getStoreId()));
        assertThrows(NullPointerException.class, () -> StoreController.getInstance().addManager(null,user2,store.getStoreId()));
        try{
            StoreController.getInstance().addManager(user1,user2,store.getStoreId());
        }catch (Exception e){
            fail();
        }
        assertTrue(store.isManager(user2));
    }


}
