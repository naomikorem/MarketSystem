package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractServiceProxy;
import Utility.LogUtility;

import java.util.List;
import java.util.Map;

public class SupplyProxy extends AbstractServiceProxy<IExternalSupplyService>
{
    /***
     * An abstract function that creates an instance of the relevant external supply service
     * @param name The name of the new service
     * @return New external supply service
     */
    @Override
    protected IExternalSupplyService ServiceFactory(String name)
    {
        return new StubSupplyService(name);

        // TODO: should we know all the services in advance?
    }

    /***
     * Call to the requested supply service and ship the items to the user
     * @param address Shipping address, The user's address
     * @param items The items that the user paid for
     * @param supply_service_name The requested external supply service
     * @return
     */
    public boolean supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name)
    {
        if(!services.containsKey(supply_service_name))
        {
            LogUtility.error("tried to use external service that doesn't exists");
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");
        }
        LogUtility.info("The supply service " + supply_service_name + " will try supply the items to the address: " + address);
        return services.get(supply_service_name).supply(address, items);
    }
}
