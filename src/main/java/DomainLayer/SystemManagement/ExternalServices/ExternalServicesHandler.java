package DomainLayer.SystemManagement.ExternalServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxy;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxy;

import java.util.List;
import java.util.Map;

public class ExternalServicesHandler
{
    private PurchaseProxy purchase_proxy;
    private SupplyProxy supply_proxy;

    private static class ExternalServicesHolder {
        static final ExternalServicesHandler INSTANCE = new ExternalServicesHandler();
    }
    private ExternalServicesHandler()
    {
        this.purchase_proxy = new PurchaseProxy();
        this.supply_proxy = new SupplyProxy();
    }

    // Implementation of thread safe singleton
    public static ExternalServicesHandler getInstance() {
        return ExternalServicesHolder.INSTANCE;
    }

    /***
     * A system admin can add external purchase service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public void addExternalPurchaseService(String name)
    {
        purchase_proxy.addService(name);
    }

    /***
     * A system admin can remove external purchase service, if the system has more than one purchase service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public void removeExternalPurchaseService(String name)
    {
        purchase_proxy.removeService(name);
    }

    /***
     * A system admin can add external supply service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public void addExternalSupplyService(String name)
    {
        supply_proxy.addService(name);
    }

    /***
     * A system admin can remove external supply service, if the system has more than one supply service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public void removeExternalSupplyService(String name)
    {
        supply_proxy.removeService(name);
    }

    /***
     * Check if the system contains purchase services
     * @return Response - true if there is at least one purchase service, false otherwise
     */
    public boolean hasPurchaseService()
    {
        return purchase_proxy.hasService();
    }

    /***
     * Check if the system contains supply services
     * @return Response - true if there is at least one supply service, false otherwise
     */
    public boolean hasSupplyService()
    {
        return supply_proxy.hasService();
    }

    /***
     * Check if the system contains purchase services with the given name
     * @return Response - true if the service was found, false otherwise
     */
    public boolean hasPurchaseService(String purchase_service_name)
    {
        return purchase_proxy.hasService(purchase_service_name);
    }

    /***
     * Check if the system contains supply services with the given name
     * @return Response - true if the service was found, false otherwise
     */
    public boolean hasSupplyService(String supply_service_name)
    {
        return supply_proxy.hasService(supply_service_name);
    }

    /***
     * Use the given purchase service name and the user's details to pay the requested amount
     * @param price The amount to pay
     * @param purchase_service_name The selected external purchase service
     */
    public void pay(double price, String purchase_service_name)
    {
        purchase_proxy.pay(price, purchase_service_name);
    }

    /***
     * Use the given supply service name and the user's details to supply the items to the user
     * @param address The User's shipping address.
     * @param items The items that the user paid for.
     * @param supply_service_name The selected external purchase service
     */
    public void supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name)
    {
        supply_proxy.supply(address, items, supply_service_name);
    }
}
