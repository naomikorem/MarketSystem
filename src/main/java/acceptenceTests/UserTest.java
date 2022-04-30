
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
        bridge.enter();
        this.u = bridge.register("user111@gmail.com", "user1", "password").getObject();
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
        assertTrue(u.getName().equals("user1"));
        assertTrue(u.getEmail().equals("user111@gmail.com"));
        u.setEmail("user123@gmail.com");
        assertFalse(u.getEmail().equals("user111@gmail.com"));
        assertTrue(u.getEmail().equals("user123@gmail.com"));
        u.setName("useruser");
        assertFalse(u.getName().equals("user1"));
        assertTrue(u.getName().equals("useruser"));
        u.setName("user1");
        bridge.logout();
    }
}
