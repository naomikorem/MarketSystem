package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.HttpClientPost;
import ServiceLayer.DTOs.SupplyParamsDTO;
import Utility.LogUtility;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class SupplyProxy extends AbstractProxy
{
    public SupplyProxy(String name, String url) {
        super(name, url);
    }

    /***
     * Call to the requested supply service and ship the items to the user
     * @param address Shipping address, The user's address
     * @param items The items that the user paid for
     * @return true if the supply service supplied the items to the user
     */
    public synchronized boolean supply(SupplyParamsDTO supplyParamsDTO, List<Map.Entry<Item, Integer>> items) throws RemoteException {
        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            LogUtility.info("Supplied the items to the user with " + this.name + " supply service to address " + supplyParamsDTO.address);
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not supply the items to the user with bad stub supply service");
            throw new RemoteException("Could not supply the items to the user with bad stub supply service");
        }

        HttpClientPost.supply(supplyParamsDTO, this.url, this.headers, this.restTemplate);
        LogUtility.info("supply success, service name: " + this.name + ", url:" + this.url);
        return true;
    }
}
