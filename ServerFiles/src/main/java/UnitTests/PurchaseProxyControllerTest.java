package UnitTests;

import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxyController;
import acceptenceTests.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class PurchaseProxyControllerTest extends AbstractTest {

    private PurchaseProxyController purchaseProxyController;

    public PurchaseProxyControllerTest()
    {
        this.purchaseProxyController = PurchaseProxyController.getInstance();
    }

    @Test
    public void supplyServiceNotExists()
    {
        //this.purchaseProxyController.addService("Moshe and Avi", "some url");
        assertThrows(IllegalArgumentException.class, () ->
                this.purchaseProxyController.pay(1000, "not exists supply"));
    }
}
