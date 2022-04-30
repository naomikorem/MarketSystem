package UnitTests;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest extends AbstractTest {
    private static User u;

    public UserTest() {
        super();
    }

    @Before
    public void setup() {
        u = new User(new SubscribedState("user@gmail.com", "user", "password"));
        UserController.getInstance().addUser(u);

    }

    @After
    public void clean() {
        UserController.getInstance().removeUser(u.getName());
    }

    @Test
    public void UserTestTest() {
        assertTrue(u.isSubscribed());
        assertTrue(u.getName().equals("user"));
        assertTrue(u.getEmail().equals("user@gmail.com"));
        u.setEmail("user123@gmail.com");
        assertFalse(u.getEmail().equals("user@gmail.com"));
        assertTrue(u.getEmail().equals("user123@gmail.com"));
        u.setName("useruser");
        assertFalse(u.getName().equals("user"));
        assertTrue(u.getName().equals("useruser"));
        u.setName("user");
    }
}
