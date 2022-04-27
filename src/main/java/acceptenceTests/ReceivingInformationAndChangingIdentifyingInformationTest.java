package acceptenceTests;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import static org.junit.Assert.*;

public class ReceivingInformationAndChangingIdentifyingInformationTest extends AbstractTest {
    public ReceivingInformationAndChangingIdentifyingInformationTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        if (!UserController.getInstance().isExist("user")) {
            bridge.register("user@gmail.com","user","user1");
        }
    }

    @AfterAll
    public void clean(){
        UserController.getInstance().removeUser("User");
    }

    @Test
    public void testAcceptReceivingInformation() {
        User u = UserController.getInstance().getUser("user");
        assertTrue(u.getName().equals("user"));
        assertTrue(u.getEmail().equals("user@gmail.com"));
        assertFalse(u.getEmail().equals("user1"));
        assertFalse(u.getEmail().equals("user"));

    }

    @Test
    public void testAcceptModifyInformation() {
        User u = UserController.getInstance().getUser("user");
        u.setName("User");
        assertFalse(u.getName().equals("user"));
        assertTrue(u.getName().equals("User1"));
        assertThrows(IllegalArgumentException.class, () -> u.setEmail("UUU"));
        assertThrows(IllegalArgumentException.class, () -> u.setName(""));
        u.setEmail("user11@gmail.com");
        assertTrue(u.getEmail().equals("user11@gmail.com"));
        assertFalse(u.getEmail().equals("user@gmail.com"));

    }

}
