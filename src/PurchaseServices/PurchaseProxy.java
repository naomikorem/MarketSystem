package PurchaseServices;

import java.util.*;

public class PurchaseProxy
{
    private final List<IExternalPurchaseService> services = Collections.synchronizedList(new ArrayList());

    public void addService(IExternalPurchaseService purchase_service)
    {
        services.add(purchase_service);
    }

    public void removeService(IExternalPurchaseService purchase_service)
    {
        services.remove(purchase_service);
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }
}
