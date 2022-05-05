package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxyController;
import Utility.LogUtility;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class PurchaseProxyController extends AbstractProxyController<PurchaseProxy>
{
    private static class PurchaseProxyControllerHolder {
        static final PurchaseProxyController INSTANCE = new PurchaseProxyController();
    }
    private PurchaseProxyController()
    {
    }

    // Implementation of thread safe singleton
    public static PurchaseProxyController getInstance() {
        return PurchaseProxyControllerHolder.INSTANCE;
    }

    /***
     * An abstract function that creates an instance of the relevant external purchase service
     * @param name The name of the new service
     * @return New external purchase service
     */
    @Override
    protected PurchaseProxy createProxy(String name, String url) throws ConnectException {
        PurchaseProxy connection = new PurchaseProxy(name);
        connection.connect(url);
        return connection;

        // TODO: should we know all the services in advance?
    }

    /***
     * Call to the requested purchase service and pay the amount with the user details.
     * @param price The amount to pay
     * @param purchase_service_name The requested external purchase service
     * @return true - if the payment process ends successfully, false otherwise.
     */
    public synchronized boolean pay(double price, String purchase_service_name) throws RemoteException {
        if (!services.containsKey(purchase_service_name))
        {
            LogUtility.error("tried to use external purchase service named " + purchase_service_name + " which does not exists in the system");
            throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.");
        }
        LogUtility.info("The purchase service " + purchase_service_name + " will try handle the payment of the user, the price: " + price);
        return services.get(purchase_service_name).pay(price);

    }
}