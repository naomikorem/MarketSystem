package acceptenceTests;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
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
        bridge.enter();
        this.u = bridge.register("user111@gmail.com", "user1", "first","last","password").getObject();
        bridge.login("user1", "password");
        bridge.logout();

    }

    @After
    public void clean() {
        UserController.getInstance().removeUser("user1");
    }

    @Test
    public void UserTestTest() {
        bridge.login("user1", "password");
        assertTrue(u.isSubscribed());
        assertEquals("user1", u.getName());
        assertEquals("user111@gmail.com", u.getEmail());
        u.setEmail("user123@gmail.com");
        assertNotEquals("user111@gmail.com", u.getEmail());
        assertEquals("user123@gmail.com", u.getEmail());
        u.setName("useruser");
        assertNotEquals("user1", u.getName());
        assertEquals("useruser", u.getName());
        u.setName("user1");
        bridge.logout();
    }
}
