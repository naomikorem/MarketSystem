package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

public class GetUserInfoTest extends AbstractTest {
    private static User u;
    private Response<User> r1, r2;

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
    public void infoTest() {

        Response<User> user = bridge.register("user111@gmail.com", "user", "","last","password");
        assertTrue(user.hadError());
        user = bridge.register("user111@gmail.com", "user", "","","password");
        assertTrue(user.hadError());
        user = bridge.register("user111@gmail.com", "user", "",null,"password");
        assertTrue(user.hadError());
        user = bridge.register("user111@gmail.com", "user", "f","last","password");
        assertFalse(user.hadError());

    }

    @Test
    public void userTestSuccess2() {
        Bridge bridge1 = new Real();
        Thread t1 = new Thread(() -> {
            bridge1.enter();
            r1 = bridge1.login("user1", "password");
        });
        Thread t2 = new Thread(() -> {
            r2 = bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertFalse(r1.hadError());
            assertFalse(r2.hadError());

            Response<User> r = bridge.getUser(u.getName());
            assertFalse(r.hadError());
            assertEquals("user111@gmail.com", r.getObject().getEmail());
            assertEquals("user1", r.getObject().getName());
            assertEquals("first", r.getObject().getFirstName());
            assertEquals("last", r.getObject().getLastName());

        } catch (Exception e) {
            fail();
        }
        bridge1.logout();
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
