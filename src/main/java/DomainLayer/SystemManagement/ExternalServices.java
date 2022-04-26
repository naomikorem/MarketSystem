package DomainLayer.SystemManagement;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.PurchaseServices.PurchaseProxy;
import DomainLayer.SystemManagement.SupplyServices.IExternalSupplyService;
import DomainLayer.SystemManagement.SupplyServices.SupplyProxy;

import java.util.LinkedList;

public class ExternalServices
{
    private PurchaseProxy purchase_proxy;
    private SupplyProxy supply_proxy;

    private static class ExternalServicesHolder {
        static final ExternalServices INSTANCE = new ExternalServices();
    }
    private ExternalServices() {}

    public static ExternalServices getInstance() {
        return ExternalServicesHolder.INSTANCE;
    }

    public void addExternalPurchaseService(String name)
    {
        purchase_proxy.addService(name);
    }

    public void removeExternalPurchaseService(String name)
    {
        purchase_proxy.removeService(name);
    }

    public void addExternalSupplyService(String supply_service_name)
    {
        supply_proxy.addService(supply_service_name);
    }

    public void removeExternalSupplyService(String name)
    {
        supply_proxy.removeService(name);
    }

    public boolean hasPurchaseService()
    {
        return purchase_proxy.hasService();
    }

    public boolean hasSupplyService()
    {
        return supply_proxy.hasService();
    }

    public void pay(double price, String purchase_service_name)
    {
        purchase_proxy.pay(price, purchase_service_name);
    }

    public void supply(String address, LinkedList<Item> items, String supply_service_name)
    {
        supply_proxy.supply(address, items, supply_service_name);
    }
}
