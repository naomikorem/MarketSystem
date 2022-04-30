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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagerTest extends AbstractTest {
    private User user1, user2, manager;
    private Store store;
    private Response<Boolean> r1,r2;

    public ManagerTest() {
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
        store = StoreController.getInstance().createStore(user1,"Store1");
        this.manager = new User(new SubscribedState("userM@gmail.com", "manager", "password"));
    }


    @Test
    public void appointUnregitered(){
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(user2,user2,store.getStoreId()));
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(user1,user1,store.getStoreId()));
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(user1,null,store.getStoreId()));
        assertThrows(IllegalArgumentException.class, () -> StoreController.getInstance().addManager(null,user2,store.getStoreId()));
        try{
            StoreController.getInstance().addManager(user1,user2,store.getStoreId());
        }catch (Exception e){
            fail();
        }
        assertTrue(store.isManager(user2));
    }


}
