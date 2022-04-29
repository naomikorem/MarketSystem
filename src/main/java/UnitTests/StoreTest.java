package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.StoreController;
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
        bridge.enter();
        if (UserController.getInstance().isExist("user")) {
            UserController.getInstance().removeUser("user");
        }
        Response<User> u = bridge.register("user1@gmail.com", "user", "password");
        this.user = u.getObject();
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