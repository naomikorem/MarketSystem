package acceptenceTests;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import static org.junit.Assert.*;

public class ReceivingInformationAndChangingIdentifyingInformationTest extends AbstractTest {

    private User u;

    public ReceivingInformationAndChangingIdentifyingInformationTest() {
        super();
    }

    @Before
    public void setup() {
        bridge.enter();
        if (UserController.getInstance().isExist("user")) {
            UserController.getInstance().removeUser("user");
        }
        this.u = bridge.register("user@gmail.com","user","user1").getObject();
    }

    @After
    public void clean(){

    }

    @Test
    public void testAcceptReceivingInformation() {
        bridge.login(u.getName(),"user1");
        assertTrue(u.getName().equals("user"));
        assertTrue(u.getEmail().equals("user@gmail.com"));
        assertFalse(u.getEmail().equals("user1"));
        assertFalse(u.getEmail().equals("user"));
        bridge.logout();
    }

    @Test
    public void testAcceptModifyInformation() {
        bridge.login(u.getName(),"user1");
        u.setName("uuser");
        assertTrue(u.getName().equals("uuser"));
        assertFalse(u.getName().equals("user"));
        u.setEmail("user11@gmail.com");
        assertTrue(u.getEmail().equals("user11@gmail.com"));
        assertFalse(u.getEmail().equals("user@gmail.com"));
        u.setName("user");
        assertTrue(u.getName().equals("user"));
        assertFalse(u.getName().equals("userr"));
        bridge.logout();

    }

}
