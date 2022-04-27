package DomainLayer.SystemManagement.PurchaseServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseProxy
{
    private final ConcurrentHashMap<String, IExternalPurchaseService> services = new ConcurrentHashMap<String, IExternalPurchaseService>();

    public void addService(String name)
    {
        synchronized (services) {
            if (services.containsKey(name))
                throw new IllegalArgumentException("The service with the name " + name + " already exists in the system.");
            services.put(name, purchaseServiceFactory(name));
        }
    }

    private IExternalPurchaseService purchaseServiceFactory(String name)
    {
        return new StubPurchaseService(name);

        // TODO: should we know all the services in advance?
    }

    public void removeService(String purchase_service_name)
    {
        synchronized (services) {
            if (services.size() == 1)
            {
                throw new IllegalArgumentException("cannot remove the purchase service " + purchase_service_name + " because it is the last connection to purchase service in the system.");
            }

            if (!services.containsKey(purchase_service_name))
                throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.");

            services.remove(purchase_service_name);
        }
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }

    public boolean hasService(String purchase_service_name)
    {
        return services.containsKey(purchase_service_name);
    }

    public boolean pay(double price, String purchase_service_name)
    {
        synchronized (services) {
            if (!services.containsKey(purchase_service_name))
                throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.");

            return services.get(purchase_service_name).pay(price);
        }
    }
}
