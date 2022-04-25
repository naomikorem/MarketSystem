package acceptenceTests;


import DomainLayer.Users.GuestState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import Utility.Utility;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest extends AbstractTest {

    public RegisterTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
    }

    @Test
    public void testAcceptRegister() {
        /**invalid email**/
        assertTrue(bridge.register("user1@gm@ail.com","user", "password").hadError());
        assertTrue(bridge.register("user1.com","user", "password").hadError());
        /**invalid name**/
        //less then 4 characters
        assertTrue(bridge.register("user1@gmail.com","use", "password").hadError());
        assertTrue(bridge.register("user1@gmail.com","", "password").hadError());
        //null value
        assertTrue(bridge.register("user1@gmail.com",null, "password").hadError());
        assertTrue(bridge.register("user1@gmail.com","U", "password").hadError());
        //contains invalid characters
        assertTrue(bridge.register("user1@gmail.com","u!", "password").hadError());
        assertTrue(bridge.register("user1@gmail.com","123", "password").hadError());
        /**invalid password**/
        //password less then 4
        assertTrue(bridge.register("user1@gmail.com","user", "p").hadError());
        //password is null
        assertTrue(bridge.register("user1@gmail.com","user", null).hadError());
        //password more then 25
        assertTrue(bridge.register("user1@gmail.com","user", "aaaaaaaaaaaaaaaaaaaaaaaaa").hadError());

    }
    @Test
    public void testNegativeRegister(){
        if(UserController.getInstance().isExist("user")){
            UserController.getInstance().removeUser("user");
        }
        assertFalse(bridge.register("user1@gmail.com","user", "password").hadError());
        //same user can't be registered twice
        assertTrue(bridge.register("user1@gmail.com","user", "password").hadError());
        assertFalse(UserController.getInstance().isExist("user1"));
        assertFalse(UserController.getInstance().isLoggedIn("user1"));
    }

}
