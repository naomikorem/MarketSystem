package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import Utility.LogUtility;

import java.rmi.AccessException;
import java.rmi.RemoteException;

public class PurchaseProxy extends AbstractProxy
{
    // add type of purchase support
    public PurchaseProxy(String name) {
        super(name);
    }

    /***
     * Interface function that all the external purchase services must have.
     * The service deals with user's payment using the relevant details (like creditcard).
     * @param amount The amount to pay
     * @return true - if the payment process is successful, false - otherwise
     */

    public boolean pay(double amount) throws RemoteException {
        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not charge the user with bad stub purchase service");
            throw new RemoteException("Could not charge the user with bad stub purchase service");
        }

        LogUtility.error("Could not charge the user with external purchase service named: " + this.name);
        throw new RemoteException("Could not charge the user with external purchase service named: " + this.name);
    }
}
