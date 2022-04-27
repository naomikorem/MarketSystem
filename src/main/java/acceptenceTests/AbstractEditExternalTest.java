package acceptenceTests;

import DomainLayer.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractEditExternalTest extends AbstractTest{
    private final String new_service_name;
    private final String new_service_name_2;

    public AbstractEditExternalTest() {
        super();
        this.new_service_name = "some external service";
        this.new_service_name_2 = "some external service2";
    }

    protected abstract Response<Boolean> hasService(String service_name);
    protected abstract Response<Boolean> addExternalService(String service_name);
    protected abstract Response<Boolean> removeExternalService(String service_name);

    @Before
    public void setup()
    {
        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());

        if (!has_service.getObject()) {
            Response<Boolean> add_service_response = addExternalService(new_service_name);
            assertFalse(add_service_response.hadError());
            assertTrue(add_service_response.getObject());
        }
    }

    @Test
    public void testAddServiceTwice()
    {
        Response<Boolean> has_service = hasService(new_service_name_2);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        // add the service for the first time
        if(!has_service.getObject()) {
            Response<Boolean> add_service_response = addExternalService(new_service_name_2);
            assertFalse(add_service_response.hadError());
            assertTrue(add_service_response.getObject());
        }

        // add the service again
        Response<Boolean> add_service_again_response = addExternalService(new_service_name_2);
        assertTrue(add_service_again_response.hadError());

        // return to base state of the system
        removeExternalService(new_service_name_2);
    }

    @Test
    public void testAddServiceThatAlreadyExists()
    {
        Response<Boolean> has_service = hasService(new_service_name);
        assertFalse(has_service.hadError());
        assertTrue(has_service.getObject());

        if(has_service.getObject())
        {
            Response<Boolean> add_service_response = addExternalService(new_service_name);
            assertTrue(add_service_response.hadError());
        }

    }

    @Test
    public void testRemoveNonExistingService()
    {
        Response<Boolean> has_service = hasService(new_service_name_2);
        assertFalse(has_service.hadError());
        assertFalse(has_service.getObject());

        if(!has_service.getObject())
        {
            Response<Boolean> remove_service_response = removeExternalService(new_service_name_2);
            assertTrue(remove_service_response.hadError());
        }
    }

    @Test
    public void testAddAndRemoveExternalServices()
    {
        Response<Boolean> has_purchase_service = hasService(new_service_name_2);
        assertFalse(has_purchase_service.hadError());
        assertFalse(has_purchase_service.getObject());

        // valid case
        Response<Boolean> add_purchase_service_response = addExternalService(new_service_name_2);
        assertFalse(add_purchase_service_response.hadError());
        assertTrue(add_purchase_service_response.getObject());

        // not valid case - add the same service twice
        assertTrue(addExternalService(new_service_name_2).hadError());

        // restore previous state
        Response<Boolean> remove_purchase_service_response = removeExternalService(new_service_name_2);
        assertFalse(remove_purchase_service_response.hadError());
        assertTrue(remove_purchase_service_response.getObject());

        // not valid case - remove the same service twice
        assertTrue(removeExternalService(new_service_name_2).hadError());
    }

    @Test
    public void testRemoveLastExternalServices()
    {
        Response<Boolean> has_purchase_service = hasService(new_service_name);
        assertFalse(has_purchase_service.hadError());
        assertTrue(has_purchase_service.getObject());

        // not valid case - remove last service in the system
        assertTrue(removeExternalService(new_service_name).hadError());
    }
}
