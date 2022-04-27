package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractServiceProxy;

import java.util.List;
import java.util.Map;

/*public class SupplyProxy
{
    private final ConcurrentHashMap<String, IExternalSupplyService> services = new ConcurrentHashMap<String, IExternalSupplyService>();

    public void addService(String name)
    {
        synchronized (services) {
            if (!services.containsKey(name))
                services.put(name, supplyServiceFactory(name));
        }
    }

    private IExternalSupplyService supplyServiceFactory(String name)
    {
        return new StubSupplyService(name);

        // TODO: should we know all the services in advance?
    }

    public void removeService(String supply_service_name)
    {
        synchronized (services) {
            if (services.size() == 1) {
                throw new IllegalArgumentException("cannot remove the supply service " + supply_service_name + " because it is the last connection to purchase service in the system.");
            }

            if (!services.containsKey(supply_service_name))
                throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");

            services.remove(supply_service_name);
        }
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }

    public boolean hasService(String supply_service_name)
    {
        return services.containsKey(supply_service_name);
    }

    public boolean supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name)
    {
        if(!services.containsKey(supply_service_name))
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");

        return services.get(supply_service_name).supply(address, items);
    }
}*/

public class SupplyProxy extends AbstractServiceProxy<IExternalSupplyService>
{

    @Override
    protected IExternalSupplyService ServiceFactory(String name) {
        return new StubSupplyService(name);

        // TODO: should we know all the services in advance?
    }

    public boolean supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name)
    {
        if(!services.containsKey(supply_service_name))
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");

        return services.get(supply_service_name).supply(address, items);
    }
}
