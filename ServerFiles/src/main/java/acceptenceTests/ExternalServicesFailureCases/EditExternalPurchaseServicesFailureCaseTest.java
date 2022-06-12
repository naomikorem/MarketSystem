package acceptenceTests.ExternalServicesFailureCases;

import DomainLayer.Response;
import acceptenceTests.Bridge;
import acceptenceTests.ExternalServicesTests.AbstractEditExternalTest;

public class EditExternalPurchaseServicesFailureCaseTest extends AbstractEditExternalFailureCaseTest {

    public EditExternalPurchaseServicesFailureCaseTest() {
        super();
    }

    @Override
    protected Response<Boolean> hasService(String service_name) {
        return bridge.hasPurchaseService(service_name);
    }

    @Override
    protected Response<Boolean> addExternalService(String service_name, String url) {
        return this.bridge.addExternalPurchaseService(service_name, url);
    }

    @Override
    protected Response<Boolean> removeExternalService(String service_name) {
        return this.bridge.removeExternalSupplyService(service_name);
    }


    @Override
    protected String getServiceName() {
        return DEFAULT_SUPPLY_NAME;
    }
}
