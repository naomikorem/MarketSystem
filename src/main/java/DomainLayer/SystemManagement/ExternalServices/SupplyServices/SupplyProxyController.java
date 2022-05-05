package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxy;
import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxyController;
import Utility.LogUtility;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class SupplyProxyController extends AbstractProxyController<SupplyProxy> {

    private static class SupplyProxyControllerHolder {
        static final SupplyProxyController INSTANCE = new SupplyProxyController();
    }
    private SupplyProxyController()
    {
    }

    // Implementation of thread safe singleton
    public static SupplyProxyController getInstance() {
        return SupplyProxyControllerHolder.INSTANCE;
    }

    /***
     * An abstract function that creates an instance of the relevant external supply service
     * @param name The name of the new service
     * @return New external supply service
     */
    @Override
    protected SupplyProxy createProxy(String name, String url) throws ConnectException {
        SupplyProxy connection = new SupplyProxy(name);
        connection.connect(url);
        return connection;
    }

    /***
     * Call to the requested supply service and ship the items to the user
     * @param address Shipping address, The user's address
     * @param items The items that the user paid for
     * @param supply_service_name The requested external supply service
     * @return
     */

    public synchronized boolean supply(String address, List<Map.Entry<Item, Integer>> items, String supply_service_name) throws RemoteException {
        if (!services.containsKey(supply_service_name)) {
            LogUtility.error("tried to use external supply service named " + supply_service_name + " which does not exists in the system");
            throw new IllegalArgumentException("The service with the name " + supply_service_name + " does not exists in the system.\n");
        }
        LogUtility.info("The supply service " + supply_service_name + " will try supply the items to the address: " + address);
        return services.get(supply_service_name).supply(address, items);

    }
}
