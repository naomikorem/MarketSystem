package acceptenceTests;

import DomainLayer.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditExternalServices extends AbstractTest {
    private final String new_service_purchase_name;
    private final String new_service2_purchase_name;

    public EditExternalServices() {
        super();
        this.new_service_purchase_name = "some external service";
        this.new_service2_purchase_name = "some external service2";
    }

    @Before
    public void setup()
    {
        Response<Boolean> has_purchase_service = bridge.hasPurchaseService(new_service_purchase_name);
        assertFalse(has_purchase_service.hadError());

        if (!has_purchase_service.getObject()) {
            Response<Boolean> add_purchase_service_response = bridge.addExternalPurchaseService(new_service_purchase_name);
            assertFalse(add_purchase_service_response.hadError());
            assertTrue(add_purchase_service_response.getObject());
        }
    }

    @Test
    public void testAddAndRemoveExternalPurchaseServices()
    {
        Response<Boolean> has_purchase_service = bridge.hasPurchaseService(new_service2_purchase_name);
        assertFalse(has_purchase_service.hadError());
        assertFalse(has_purchase_service.getObject());

        // valid case
        Response<Boolean> add_purchase_service_response = bridge.addExternalPurchaseService(new_service2_purchase_name);
        assertFalse(add_purchase_service_response.hadError());
        assertTrue(add_purchase_service_response.getObject());

        // not valid case
        assertTrue(bridge.addExternalPurchaseService(new_service2_purchase_name).hadError());

        // restore previous state
        Response<Boolean> remove_purchase_service_response = bridge.removeExternalPurchaseService(new_service2_purchase_name);
        assertFalse(remove_purchase_service_response.hadError());
        assertTrue(remove_purchase_service_response.getObject());

        // not valid case
        assertTrue(bridge.removeExternalPurchaseService(new_service2_purchase_name).hadError());
    }

    @Test
    public void testRemoveLastExternalPurchaseServices()
    {
        Response<Boolean> has_purchase_service = bridge.hasPurchaseService(new_service_purchase_name);
        assertFalse(has_purchase_service.hadError());
        assertTrue(has_purchase_service.getObject());

        // not valid case
        assertTrue(bridge.removeExternalPurchaseService(new_service_purchase_name).hadError());
    }

}
