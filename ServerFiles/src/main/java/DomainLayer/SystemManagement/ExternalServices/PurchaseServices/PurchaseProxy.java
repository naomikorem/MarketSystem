package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.HttpClientPost;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import Utility.LogUtility;

import java.rmi.RemoteException;

public class PurchaseProxy extends AbstractProxy
{
    // add type of purchase support
    public PurchaseProxy(String name, String url) {
        super(name, url);
    }

    /***
     * Interface function that all the external purchase services must have.
     * The service deals with user's payment using the relevant details (like creditcard).
     * @param amount The amount to pay
     * @return true - if the payment process is successful, false - otherwise
     */
    public boolean pay(double amount, PaymentParamsDTO paymentParamsDTO) throws RemoteException
    {
        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            LogUtility.info("Charge the user with " + this.name + " purchase service with " + amount + " shekels");
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not charge the user with bad stub purchase service");
            throw new RemoteException("Could not charge the user with bad stub purchase service");
        }


        HttpClientPost.pay(paymentParamsDTO, this.url, this.headers, this.restTemplate);
        LogUtility.info("payment success, service name: " + this.name + ", url:" + this.url);
        return true;
    }
}
