package DomainLayer.SystemManagement.ExternalServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxyController;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxyController;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ExternalServicesHandler
{
    private PurchaseProxyController purchaseProxyController;
    private SupplyProxyController supplyProxyController;

    private static class ExternalServicesHolder
    {
        static final ExternalServicesHandler INSTANCE = new ExternalServicesHandler();
    }
    private ExternalServicesHandler()
    {
        this.purchaseProxyController = PurchaseProxyController.getInstance();
        this.supplyProxyController = SupplyProxyController.getInstance();
    }

    // Implementation of thread safe singleton
    public static ExternalServicesHandler getInstance() {
        return ExternalServicesHolder.INSTANCE;
    }

    public void clearServices()
    {
        this.purchaseProxyController.clearServices();
        this.supplyProxyController.clearServices();
    }

    /***
     * A system admin can add external purchase service, if it doesn't already exist.
     * @param name The name of the new service
     * @param url The connection to the external service.
     * @return Response - if the addition succeeded or if there was an error
     */
    public void addExternalPurchaseService(String name, String url) throws ConnectException {
        purchaseProxyController.addService(name, url);
    }

    /***
     * A system admin can remove external purchase service, if the system has more than one purchase service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public void removeExternalPurchaseService(String name)
    {
        purchaseProxyController.removeService(name);
    }

    /***
     * A system admin can add external supply service, if it doesn't already exist.
     * @param name The name of the new service
     * @param url The connection to the external service.
     * @return Response - if the addition succeeded or if there was an error
     */
    public void addExternalSupplyService(String name, String url) throws ConnectException {
        supplyProxyController.addService(name, url);
    }

    /***
     * A system admin can remove external supply service, if the system has more than one supply service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public void removeExternalSupplyService(String name)
    {
        supplyProxyController.removeService(name);
    }

    /***
     * Check if the system contains at least one purchase services
     * @return Response - true if there is at least one purchase service, false otherwise
     */
    public boolean hasPurchaseService()
    {
        return purchaseProxyController.hasService();
    }

    /***
     * Check if the system contains at least one supply services
     * @return Response - true if there is at least one supply service, false otherwise
     */
    public boolean hasSupplyService()
    {
        return supplyProxyController.hasService();
    }

    /***
     * Check if the system contains purchase services with the given name
     * @param purchase_service_name - The name of the external service
     * @return Response - true if the service was found, false otherwise
     */
    public boolean hasPurchaseService(String purchase_service_name)
    {
        return purchaseProxyController.hasService(purchase_service_name);
    }

    /***
     * Check if the system contains supply services with the given name
     * @param supply_service_name - The name of the external service
     * @return Response - true if the service was found, false otherwise
     */
    public boolean hasSupplyService(String supply_service_name)
    {
        return supplyProxyController.hasService(supply_service_name);
    }

    /***
     * Use the given purchase service name and the user's details to pay the requested amount
     * @param price The amount to pay
     * @param purchase_service_name The selected external purchase service
     * return true if the payment succeeded.
     */
    public boolean pay(double price, String purchase_service_name) throws RemoteException {
        return purchaseProxyController.pay(price, purchase_service_name);
    }

    /***
     * Use the given supply service name and the user's details to supply the items to the user
     * @param address The User's shipping address.
     * @param items The items that the user paid for.
     * @param supply_service_name The selected external purchase service
     * return true if the supply company shipped the items.
     */
    public boolean supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name) throws RemoteException {
        return supplyProxyController.supply(address, items, supply_service_name);
    }
}
