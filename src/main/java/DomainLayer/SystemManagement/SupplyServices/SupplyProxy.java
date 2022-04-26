package DomainLayer.SystemManagement.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.PurchaseServices.IExternalPurchaseService;
import DomainLayer.SystemManagement.PurchaseServices.StubPurchaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SupplyProxy
{
    private final ConcurrentHashMap<String, IExternalSupplyService> services = new ConcurrentHashMap<String, IExternalSupplyService>();

    public void addService(String name)
    {
        if(!services.containsKey(name))
            services.put(name, supplyServiceFactory(name));
    }

    private IExternalSupplyService supplyServiceFactory(String name)
    {
        if (name.equals("stub"))
            return new StubSupplyService(name);

        // TODO: should we know all the services in advance?
        return null;
    }

    public void removeService(String supply_service_name)
    {
        if(!services.containsKey(supply_service_name))
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");

        services.remove(supply_service_name);
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }

    public boolean hasService(String purchase_service_name)
    {
        return services.containsKey(purchase_service_name);
    }

    public boolean supply(String address, List<Item> items, String supply_service_name)
    {
        if(!services.containsKey(supply_service_name))
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");

        return services.get(supply_service_name).supply(address, items);
    }
}
