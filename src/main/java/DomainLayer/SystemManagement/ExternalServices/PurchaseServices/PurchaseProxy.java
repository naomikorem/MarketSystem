package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DomainLayer.SystemManagement.ExternalServices.AbstractServiceProxy;
import Utility.LogUtility;

public class PurchaseProxy extends AbstractServiceProxy<IExternalPurchaseService>
{
    /***
     * An abstract function that creates an instance of the relevant external purchase service
     * @param name The name of the new service
     * @return New external purchase service
     */
    @Override
    protected IExternalPurchaseService ServiceFactory(String name)
    {
        return new StubPurchaseService(name);

        // TODO: should we know all the services in advance?
    }

    /***
     * Call to the requested purchase service and pay the amount with the user details.
     * @param price The amount to pay
     * @param purchase_service_name The requested external purchase service
     * @return true - if the payment process ends successfully, false otherwise.
     */
    public synchronized boolean pay(double price, String purchase_service_name)
    {
        if (!services.containsKey(purchase_service_name))
        {
            LogUtility.error("tried to use external service that doesn't exists");
            throw new IllegalArgumentException("The service with the name " + purchase_service_name + " does not exists in the system.");
        }
        LogUtility.info("The purchase service " + purchase_service_name + " will try handle the payment of the user, the price: " + price);
        return services.get(purchase_service_name).pay(price);

    }
}