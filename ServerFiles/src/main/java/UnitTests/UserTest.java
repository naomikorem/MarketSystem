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

import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends AbstractTest {
    private static User u;

    public UserTest() {
        super();
    }

    @Before
    public void setup() {
        u = new User(new SubscribedState("user@gmail.com", "user","first", "last", "password"));

    }

    @Test
    public void UserTestTest() {
        assertTrue(u.isSubscribed());
        assertEquals("user", u.getName());
        assertEquals("user@gmail.com", u.getEmail());
        u.setEmail("user123@gmail.com");
        assertNotEquals("user@gmail.com", u.getEmail());
        assertEquals("user123@gmail.com", u.getEmail());
        u.setName("useruser");
        assertNotEquals("user", u.getName());
        assertEquals("useruser", u.getName());
        u.setName("user");
    }
}
