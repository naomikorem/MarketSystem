package acceptenceTests;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        this.u = bridge.register("user@gmail.com","user","first","last","user1").getObject();
    }

    @After
    public void clean(){

    }

    @Test
    public void testAcceptReceivingInformation() {
        bridge.login(u.getName(),"user1");
        assertEquals("user", u.getName());
        assertEquals("user@gmail.com", u.getEmail());
        assertNotEquals("user1", u.getEmail());
        assertNotEquals("user", u.getEmail());
        bridge.logout();
    }

    @Test
    public void testAcceptModifyInformation() {
        bridge.login(u.getName(),"user1");
        bridge.setUserName("uuser");
        assertEquals("uuser", u.getName());
        assertNotEquals("user", u.getName());
        u.setEmail("user11@gmail.com");
        assertEquals("user11@gmail.com", u.getEmail());
        assertNotEquals("user@gmail.com", u.getEmail());
        bridge.logout();

    }

}
