package acceptenceTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTest extends AbstractTest {

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

}
