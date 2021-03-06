package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Users.User;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RegisterTest extends AbstractTest {
    private Response<User> r1, r2;

    public RegisterTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
    }

    @After
    public void clean() {
        bridge.logout();
    }

    @Test
    public void testAcceptRegister() {
        //invalid email
        assertTrue(bridge.register("user1@gm@ail.com", "user","first","last", "password").hadError());
        assertTrue(bridge.register("user1.com", "user", "first","last","password").hadError());
        //invalid name
        //less then 4 characters
        assertTrue(bridge.register("user1@gmail.com", "use", "first","last","password").hadError());
        assertTrue(bridge.register("user1@gmail.com", "", "first","last","password").hadError());
        //null value
        assertTrue(bridge.register("user1@gmail.com", null, "first","last","password").hadError());
        assertTrue(bridge.register("user1@gmail.com", "U", "first","last","password").hadError());
        //contains invalid characters
        assertTrue(bridge.register("user1@gmail.com", "u!", "first","last","password").hadError());
        assertTrue(bridge.register("user1@gmail.com", "123","first","last", "password").hadError());
        //invalid password
        //password less then 4
        assertTrue(bridge.register("user1@gmail.com", "user","first","last", "p").hadError());
        //password is null
        assertTrue(bridge.register("user1@gmail.com", "user", "first","last",null).hadError());
        //password more then 25
        assertTrue(bridge.register("user1@gmail.com", "user","first","last", "aaaaaaaaaaaaaaaaaaaaaaaaaa").hadError());

    }

    @Test
    public void testNegativeRegister() {

        Response<User> u = bridge.register("user1@gmail.com", "user","first","last", "password");
        assertFalse(u.hadError());
        //same user can't be registered twice
        assertTrue(bridge.register("user1@gmail.com", "user", "first","last","password").hadError());

    }

    @Test
    public void synchronizedRegisterTest() {
        Thread t1 = new Thread(() -> {
            r1 = bridge.register("user1@gmail.com", "user","first","last", "password");
        });
        Thread t2 = new Thread(() -> {
            r2 = bridge.register("user1@gmail.com", "user", "first","last","password");
        });
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
