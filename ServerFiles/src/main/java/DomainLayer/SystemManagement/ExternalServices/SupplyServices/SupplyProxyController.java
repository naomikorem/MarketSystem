package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DataLayer.DALObjects.ServiceDAL;
import DataLayer.ServiceType;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import ServiceLayer.DTOs.SupplyParamsDTO;
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
    protected SupplyProxy createProxy(String name, String url) throws ConnectException
    {
        SupplyProxy connection = new SupplyProxy(name, url);
        connection.connect();
        return connection;
    }

    @Override
    protected ServiceDAL createServiceDALObject(String name, String url)
    {
        ServiceDAL service = new ServiceDAL();
        service.setName(name);
        service.setUrl(url);
        service.setServiceType(ServiceType.Supply);
        return service;
    }

    /***
     * Call to the requested supply service and ship the items to the user
     * @param items The items that the user paid for
     * @return
     */
    public synchronized boolean supply(SupplyParamsDTO supplyParamsDTO, List<Map.Entry<Item, Integer>> items) throws RemoteException {
        if (!services.containsKey(supplyParamsDTO.ServiceName))
        {
            LogUtility.error("tried to use external supply service named " + supplyParamsDTO.ServiceName + " which does not exists in the system");
            throw new IllegalArgumentException("The service with the name " + supplyParamsDTO.ServiceName + " does not exists in the system.\n");
        }
        LogUtility.info("The supply service " + supplyParamsDTO.ServiceName + " will try supply the items to the address: " + supplyParamsDTO.address);
        return services.get(supplyParamsDTO.ServiceName).supply(supplyParamsDTO, items);
    }
}
