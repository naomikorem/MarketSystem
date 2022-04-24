package acceptenceTests;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTest extends AbstractTest {

    public LoginTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.register("user111@gmail.com", "user", "password");
    }

    @Test
    public void testLogin() {
        assertTrue(bridge.login("user1", "password").hadError());
        assertTrue(bridge.login("user", "password1").hadError());
        assertTrue(bridge.login( null, "password").hadError());
        assertTrue(bridge.login("user", null).hadError());
        assertFalse(bridge.login("user", "password").hadError());
        assertTrue(bridge.login("user", "password").hadError());
    }

}
