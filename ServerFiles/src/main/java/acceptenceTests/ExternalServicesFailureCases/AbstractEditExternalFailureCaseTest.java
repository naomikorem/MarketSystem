package acceptenceTests.ExternalServicesFailureCases;

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
import static org.junit.Assert.assertTrue;

public abstract class AbstractEditExternalFailureCaseTest extends AbstractTest{

    private final String new_service_name1;

    private final String new_service_name2;

    private final String anotherAdminUsername;

    private Response<Boolean> res1, res2;

    public AbstractEditExternalFailureCaseTest() {
        super();
        this.new_service_name1 = AbstractProxy.BAD_STUB_NAME;
        this.new_service_name2 = AbstractProxy.GOOD_STUB_NAME;
        this.anotherAdminUsername = "someAdmin12";
    }

    protected abstract Response<Boolean> hasService(String service_name);
    protected abstract Response<Boolean> addExternalService(String service_name, String url);
    protected abstract Response<Boolean> removeExternalService(String service_name);
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
    public void testAddService()
    {
        Response<Boolean> has_service_again = hasService(getServiceName());
        assertFalse(has_service_again.hadError());

        for(int i = 0 ; i< 1000 ; i++)
        {
            String val = getServiceName();
            Response<Boolean> add_service_response = addExternalService(getServiceName(), "url");
            assertTrue(add_service_response.hadError());
        }
    }

    @Test
    public void testRemoveNonExistingService()
    {
        Response<Boolean> has_service = hasService(new_service_name1);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        if(!has_service.getObject())
        {
            Response<Boolean> remove_service_response = removeExternalService(new_service_name1);
            assertTrue(remove_service_response.hadError());
        }

        has_service = hasService(new_service_name2);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        if(!has_service.getObject())
        {
            Response<Boolean> remove_service_response = removeExternalService(new_service_name2);
            assertTrue(remove_service_response.hadError());
        }
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
}

