package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest extends AbstractTest {

    private Response<User> r1, r2;

    public LoginTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user", "password");
    }

    @After
    public void afterEach() {
        bridge.logout();
    }

    @Test
    public void testLoginSuccess() {
        assertFalse(bridge.login("user", "password").hadError());
    }

    @Test
    public void testLoginLoggedUser() {
        assertFalse(bridge.login("user", "password").hadError());
        assertTrue(bridge.login("user", "password").hadError());
    }

    @Test
    public void testLoginBadCredentials() {
        assertTrue(bridge.login("user1", "password").hadError());
        assertTrue(bridge.login("user", "password1").hadError());
        assertTrue(bridge.login(null, "password").hadError());
        assertTrue(bridge.login("user", null).hadError());
    }

    @Test
    public void synchronizedLoginTest() {
        Thread t1 = new Thread(() -> r1 = bridge.login("user", "password"));
        Thread t2 = new Thread(() -> r2 = bridge.login("user", "password"));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            assertTrue(r1.hadError() || r2.hadError()); //one failed to connect
            assertFalse(r1.hadError() && r2.hadError()); //not both failed
        } catch (Exception e) {
            fail();
        }
    }
}
