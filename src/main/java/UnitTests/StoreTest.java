package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest extends AbstractTest {

    private User user;

    public StoreTest() {
        super();
    }

    @Before
    public void setup() {
        user = new User(new SubscribedState("user@gmail.com", "user", "password"));
        UserController.getInstance().addUser(user);
    }

    @After
    public void clean() {
        UserController.getInstance().removeUser("user");
    }

    @Test
    public void UnitTestStore() {
        assertThrows(IllegalArgumentException.class , () -> StoreController.getInstance().createStore(user, null));
        assertThrows(IllegalArgumentException.class , () -> StoreController.getInstance().createStore(user, ""));

    }


}
