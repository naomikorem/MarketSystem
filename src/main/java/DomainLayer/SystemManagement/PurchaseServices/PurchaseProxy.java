package DomainLayer.SystemManagement.PurchaseServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseProxy
{
    private final ConcurrentHashMap<String, IExternalPurchaseService> services = new ConcurrentHashMap<String, IExternalPurchaseService>();

    public void addService(String name)
    {
        if(!services.containsKey(name))
            services.put(name, purchaseServiceFactory(name));
    }

    private IExternalPurchaseService purchaseServiceFactory(String name)
    {
        if (name.equals("stub"))
            return new StubPurchaseService(name);

        // TODO: should we know all the services in advance?
        return null;
    }

    public void removeService(String purchase_service_name)
    {
        if(!services.containsKey(purchase_service_name))
            throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.\n");

        services.remove(purchase_service_name);
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }

    public boolean pay(double price, String purchase_service_name)
    {
        if(!services.containsKey(purchase_service_name))
            throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.\n");

        return services.get(purchase_service_name).pay(price);
    }
}
