package acceptenceTests.ExternalServicesFailureCases;

import DomainLayer.Response;
import acceptenceTests.Bridge;
import acceptenceTests.ExternalServicesFailureCases.AbstractEditExternalFailureCaseTest;

public class EditExternalSupplyServicesFailureCaseTest extends AbstractEditExternalFailureCaseTest {

    public EditExternalSupplyServicesFailureCaseTest(String new_service_name2) {
        super(new_service_name2);
    }

    @Override
    protected Response<Boolean> hasService(String service_name) {
        return bridge.hasSupplyService(service_name);
    }

    @Override
    protected Response<Boolean> addExternalService(String service_name, String url) {
        return this.bridge.addExternalSupplyService(service_name, url);
    }

    @Override
    protected Response<Boolean> addExternalService(Bridge bridge, String service_name, String url) {
        return bridge.addExternalSupplyService(service_name, url);
    }

    @Override
    protected Response<Boolean> removeExternalService(String service_name) {
        return this.bridge.removeExternalSupplyService(service_name);
    }

    @Override
    protected Response<Boolean> removeExternalService(Bridge bridge, String service_name) {
        return bridge.removeExternalSupplyService(service_name);
    }

    @Override
    protected String getServiceName() {
        return DEFAULT_SUPPLY_NAME;
    }
}
