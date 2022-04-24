package acceptenceTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends AbstractTest {

    public LoginTest() {
        super();
    }

    @BeforeEach
    public void setup() {
        this.bridge.register("user111@gmail.com", "user", "password");
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
