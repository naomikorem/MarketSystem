package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import Utility.LogUtility;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class SupplyProxy extends AbstractProxy
{
    public SupplyProxy(String name) {
        super(name);
    }

    /***
     * Call to the requested supply service and ship the items to the user
     * @param address Shipping address, The user's address
     * @param items The items that the user paid for
     * @return
     */
    public synchronized boolean supply(String address, List<Map.Entry<Item, Integer>> items) throws RemoteException {
        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not supply the items to the user with bad stub supply service");
            throw new RemoteException("Could not supply the items to the user with bad stub supply service");
        }

        LogUtility.error("Could not supply the items to the user with external supply service named: " + this.name);
        throw new RemoteException("Could not supply the items to the user with external supply service named: " + this.name);
    }
}
