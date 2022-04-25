package SupplyServices;

import PurchaseServices.IExternalPurchaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SupplyProxy
{
    private final List<IExternalSupplyService> services = Collections.synchronizedList(new ArrayList());

    public void addService(IExternalSupplyService supply_service)
    {
        services.add(supply_service);
    }

    public void removeService(IExternalSupplyService supply_service)
    {
        services.remove(supply_service);
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }
}
