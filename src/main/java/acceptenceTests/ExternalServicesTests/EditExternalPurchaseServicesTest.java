package acceptenceTests.ExternalServicesTests;

import DomainLayer.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditExternalPurchaseServicesTest extends AbstractEditExternalTest {

    public EditExternalPurchaseServicesTest() {
        super();
    }

    @Override
    protected Response<Boolean> hasService(String service_name) {
        return bridge.hasPurchaseService(service_name);
    }

    @Override
    protected Response<Boolean> addExternalService(String service_name) {
        return bridge.addExternalPurchaseService(service_name);
    }

    @Override
    protected Response<Boolean> removeExternalService(String service_name) {
        return bridge.removeExternalPurchaseService(service_name);
    }
}
