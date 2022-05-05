package acceptenceTests.ExternalServicesTests;

import DomainLayer.Response;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractEditExternalTest extends AbstractTest {
    private final String new_service_name;
    //private final String new_service_name_2;

    public AbstractEditExternalTest() {
        super();
        this.new_service_name = AbstractProxy.GOOD_STUB_NAME_2;
       // this.new_service_name_2 = "some external service2";
    }

    protected abstract Response<Boolean> hasService(String service_name);
    protected abstract Response<Boolean> addExternalService(String service_name, String url);
    protected abstract Response<Boolean> removeExternalService(String service_name);

    @Before
    public void setup()
    {
        bridge.enter();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        /*Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());

        if (!has_service.getObject()) {
            Response<Boolean> add_service_response = addExternalService(new_service_name, "url");
            assertFalse(add_service_response.hadError());
            assertTrue(add_service_response.getObject());
        }*/
    }

    @After
    public void tearDown(){
        bridge.logout();
    }

    @Test
    public void testAddServiceTwice()
    {
        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        // add the service for the first time
        if(!has_service.getObject()) {
            Response<Boolean> add_service_response = addExternalService(new_service_name, "url");
            assertFalse(add_service_response.hadError());
            assertTrue(add_service_response.getObject());
        }

        // add the service again
        Response<Boolean> add_service_again_response = addExternalService(new_service_name, "url");
        assertTrue(add_service_again_response.hadError());

        // return to base state of the system
        removeExternalService(new_service_name);
    }

    @Test
    public void testAddServiceThatAlreadyExists()
    {
        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        // add for the first time
        if(!has_service.getObject())
        {
            Response<Boolean> add_service_response = addExternalService(new_service_name, "url");
            assertFalse(add_service_response.hadError());
        }

        Response<Boolean> has_service_again = hasService(new_service_name);
        assertFalse(has_service_again.hadError());
        assertTrue(has_service_again.getObject());

        if(has_service_again.getObject())
        {
            Response<Boolean> add_service_response = addExternalService(new_service_name, "url");
            assertTrue(add_service_response.hadError());
        }
    }

    @Test
    public void testRemoveNonExistingService()
    {
        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        if(!has_service.getObject())
        {
            Response<Boolean> remove_service_response = removeExternalService(new_service_name);
            assertTrue(remove_service_response.hadError());
        }
    }

    @Test
    public void testAddAndRemoveExternalServices()
    {
        Response<Boolean> has_purchase_service = hasService(new_service_name);
        assertFalse(has_purchase_service.hadError());
        assertFalse(has_purchase_service.getObject());

        // valid case
        Response<Boolean> add_purchase_service_response = addExternalService(new_service_name, "url");
        assertFalse(add_purchase_service_response.hadError());
        assertTrue(add_purchase_service_response.getObject());

        // not valid case - add the same service twice
        assertTrue(addExternalService(new_service_name, "url").hadError());

        // restore previous state
        Response<Boolean> remove_purchase_service_response = removeExternalService(new_service_name);
        assertFalse(remove_purchase_service_response.hadError());
        assertTrue(remove_purchase_service_response.getObject());

        // not valid case - remove the same service twice
        assertTrue(removeExternalService(new_service_name).hadError());
    }

    @Test
    public void testRemoveLastExternalServices()
    {
        Response<Boolean> add_service_response = addExternalService(new_service_name, "url");
        assertFalse(add_service_response.hadError());
        assertTrue(add_service_response.getObject());

        Response<Boolean> remove_first_stub_res = removeExternalService(AbstractProxy.GOOD_STUB_NAME);
        assertFalse(remove_first_stub_res.hadError());
        assertTrue(remove_first_stub_res.getObject());

        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertTrue(has_service.getObject());

        // not valid case - remove last service in the system
        assertTrue(removeExternalService(new_service_name).hadError());
    }

    @Test
    public void removeServiceGuestUser()
    {
        bridge.logout();
        assertTrue(removeExternalService(new_service_name).hadError());
    }

    @Test
    public void addServiceGuestUser()
    {
        bridge.logout();
        assertTrue(addExternalService(new_service_name, "url").hadError());
    }
}
