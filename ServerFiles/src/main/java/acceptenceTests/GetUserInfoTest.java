package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserInfoTest extends AbstractTest {
    private static User u;

    public GetUserInfoTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        u = bridge.register("user111@gmail.com", "user1", "first","last","password").getObject();
    }

    @Test
    public void userTestSuccess() {
        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        Response<User> r = bridge.getUser(u.getName());
        assertFalse(r.hadError());
        assertEquals("user111@gmail.com", r.getObject().getEmail());
        assertEquals("user1", r.getObject().getName());
        assertEquals("first", r.getObject().getFirstName());
        assertEquals("last", r.getObject().getLastName());
    }

    @Test
    public void nonexistentUserTest() {
        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        assertTrue(bridge.getUser("NonExistent").hadError());
    }

    @Test
    public void illegalRequestTest() {
        Response<User> r = bridge.getUser(u.getName());
        assertTrue(r.hadError());
    }
}
