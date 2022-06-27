package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DataLayer.DALObjects.ServiceDAL;
import DataLayer.ServiceType;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxy;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import Utility.LogUtility;
import lombok.SneakyThrows;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.List;

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
    protected ServiceDAL toDAL(String name, String url) {
        ServiceDAL service = new ServiceDAL();
        service.setName(name);
        service.setUrl(url);
        service.setServiceType(ServiceType.Purchase);
        return service;
    }

    @Override
    public void loadAllServices() {
        List<ServiceDAL> serviceDALS = manager.getAllServicesByType(ServiceType.Purchase);
        for (ServiceDAL s: serviceDALS)
        {
            String name = s.getName();
            PurchaseProxy service_domain = toDomain(s);
            this.services.put(name, service_domain);
        }
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