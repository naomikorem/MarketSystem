package UnitTests;

import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxy;
import acceptenceTests.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class PurchaseProxyTest extends AbstractTest {

    private PurchaseProxy purchaseProxy;

    public PurchaseProxyTest()
    {
        this.purchaseProxy = new PurchaseProxy();
    }

    @Test
    public void supplyServiceNotExists()
    {
        this.purchaseProxy.addService("Moshe and Avi");
        assertThrows(IllegalArgumentException.class, () ->
                this.purchaseProxy.pay(1000, "another supply"));
    }
}
