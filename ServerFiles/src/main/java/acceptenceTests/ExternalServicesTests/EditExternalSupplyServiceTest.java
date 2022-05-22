package acceptenceTests.ExternalServicesTests;

import DomainLayer.Response;
import acceptenceTests.Bridge;

public class EditExternalSupplyServiceTest extends AbstractEditExternalTest{

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
}
