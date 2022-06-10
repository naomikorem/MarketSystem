package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DataLayer.DALObjects.ServiceDAL;
import DataLayer.ServiceType;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import Utility.LogUtility;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class PurchaseProxyController extends AbstractProxyController<PurchaseProxy>
{
    private static class PurchaseProxyControllerHolder
    {
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
        PurchaseProxy connection = new PurchaseProxy(name, url);
        connection.connect();
        return connection;
    }

    @Override
    protected ServiceDAL createServiceDALObject(String name, String url) {
        ServiceDAL service = new ServiceDAL();
        service.setName(name);
        service.setUrl(url);
        service.setServiceType(ServiceType.Purchase);
        return service;
    }

    /***
     * Call to the requested purchase service and pay the amount with the user details.
     * @param price The amount to pay
     * @return true - if the payment process ends successfully, false otherwise.
     */
    public synchronized boolean pay(double price, PaymentParamsDTO paymentParamsDTO) throws RemoteException {
        if (!services.containsKey(paymentParamsDTO.ServiceName))
        {
            LogUtility.error("tried to use external purchase service named " + paymentParamsDTO.ServiceName + " which does not exists in the system");
            throw new IllegalArgumentException("The service with the name " + paymentParamsDTO.ServiceName + " does not exists in the system.");
        }
        LogUtility.info("The purchase service " + paymentParamsDTO.ServiceName + " will try handle the payment of the user, the price: " + price);
        return services.get(paymentParamsDTO.ServiceName).pay(price, paymentParamsDTO);
    }
}