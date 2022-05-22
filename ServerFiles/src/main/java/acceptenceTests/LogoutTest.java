package acceptenceTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogoutTest extends AbstractTest {

    public LogoutTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user111@gmail.com", "user","first","last", "password");
    }

    @After
    public void afterEach() {
        bridge.logout();
    }

    @Test
    public void testLogoutSuccess() {
        bridge.login("user", "password");
        assertFalse(bridge.logout().hadError());
    }

    @Test
    public void testLogoutFailure() {
        assertTrue(bridge.logout().hadError());
    }

}
