package acceptenceTests.ExternalServicesTests;

import DomainLayer.Response;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import acceptenceTests.Bridge;
import acceptenceTests.Real;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractEditExternalTest extends AbstractTest {
    private final String new_service_name;

    private final String anotherAdminUsername;

    private Response<Boolean> res1, res2;

    public AbstractEditExternalTest() {
        super();
        this.new_service_name = AbstractProxy.GOOD_STUB_NAME_2;
        this.anotherAdminUsername = "someAdmin12";
    }

    protected abstract Response<Boolean> hasService(String service_name);
    protected abstract Response<Boolean> addExternalService(String service_name, String url);
    protected abstract Response<Boolean> addExternalService(Bridge bridge, String service_name, String url);
    protected abstract Response<Boolean> removeExternalService(String service_name);
    protected abstract Response<Boolean> removeExternalService(Bridge bridge, String service_name);
    protected abstract String getServiceName();

    @Before
    public void setup()
    {
        bridge.enter();
        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        assertFalse(bridge.register("adminMail@gmail.com", anotherAdminUsername,"first", "last", "pass").hadError());
        assertFalse(bridge.addAdmin(anotherAdminUsername).hadError());
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
        Response<Boolean> has_service_again = hasService(getServiceName());
        assertFalse(has_service_again.hadError());
        assertTrue(has_service_again.getObject());

        Response<Boolean> add_service_response = addExternalService(getServiceName(), "url");
        assertTrue(add_service_response.hadError());
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

        Response<Boolean> remove_first_stub_res = removeExternalService(getServiceName());
        assertFalse(remove_first_stub_res.hadError());
        assertTrue(remove_first_stub_res.getObject());

        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertTrue(has_service.getObject());

        // not valid case - remove last service in the system
        assertTrue(removeExternalService(new_service_name).hadError());
    }

    @Test
    public void FailedToAddService_ConnectionFailed()
    {
        Response<Boolean> add_service_response = addExternalService(AbstractProxy.BAD_STUB_NAME, "url");
        assertTrue(add_service_response.hadError());

        Response<Boolean> has_service1 = hasService(AbstractProxy.BAD_STUB_NAME);
        assertFalse(has_service1.hadError());
        assertFalse(has_service1.getObject());

        Response<Boolean> remove_first_stub_res = removeExternalService(AbstractProxy.BAD_STUB_NAME);
        assertTrue(remove_first_stub_res.hadError());

        Response<Boolean> has_service = hasService(AbstractProxy.BAD_STUB_NAME);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());
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

    @Test
    public void addServiceSimultaneously()
    {
        bridge.logout();
        for (int i = 0; i < 100; i++) {
            Thread t1 = new Thread(() -> {
                assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
                res1 = addExternalService(AbstractProxy.GOOD_STUB_NAME_2, "url");
                bridge.logout();
            });
            Thread t2 = new Thread(() -> {
                Bridge bridge_new_admin = new Real();
                bridge_new_admin.enter();
                assertFalse(bridge_new_admin.login(anotherAdminUsername, "pass").hadError());
                res2 = addExternalService(bridge_new_admin, AbstractProxy.GOOD_STUB_NAME_2, "url");
                bridge_new_admin.logout();
            });
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
                assertTrue(res1.hadError() || res2.hadError());
                assertFalse(res1.hadError() && res2.hadError());
            } catch (Exception e) {
                fail(e.getMessage());
            }

            assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
            removeExternalService(AbstractProxy.GOOD_STUB_NAME_2);
            bridge.logout();
        }
    }

    @Test
    public void removeServiceSimultaneously()
    {
        Response<Boolean> addServiceResponse = addExternalService(AbstractProxy.GOOD_STUB_NAME_2, "url");
        addExternalService(AbstractProxy.GOOD_STUB_NAME, "url");
        assertFalse(addServiceResponse.hadError());
        assertTrue(hasService(AbstractProxy.GOOD_STUB_NAME_2).getObject());
        assertTrue(hasService(AbstractProxy.GOOD_STUB_NAME).getObject());
        bridge.logout();

        for (int i = 0; i < 200; i++) {
            Thread t1 = new Thread(() -> {
                assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
                res1 = removeExternalService(AbstractProxy.GOOD_STUB_NAME_2);
                bridge.logout();
            });
            Thread t2 = new Thread(() -> {
                Bridge bridge_new_admin = new Real();
                bridge_new_admin.enter();
                assertFalse(bridge_new_admin.login(anotherAdminUsername, "pass").hadError());
                res2 = removeExternalService(bridge_new_admin, AbstractProxy.GOOD_STUB_NAME_2);
                bridge_new_admin.logout();
            });
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
                assertTrue(res1.hadError() || res2.hadError());
                assertFalse(res1.hadError() && res2.hadError());
            } catch (Exception e) {
                fail(e.getMessage());
            }

            assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
            addServiceResponse = addExternalService(AbstractProxy.GOOD_STUB_NAME_2, "url");
            assertFalse(addServiceResponse.hadError());
            assertTrue(hasService(AbstractProxy.GOOD_STUB_NAME_2).getObject());
            assertTrue(hasService(AbstractProxy.GOOD_STUB_NAME).getObject());
            bridge.logout();
        }
    }
}
