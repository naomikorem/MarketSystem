package UnitTests;

import DomainLayer.SystemManagement.ExternalServices.PurchaseServices.PurchaseProxyController;
import ServiceLayer.DTOs.PaymentParamsDTO;
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
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                "not existing service",
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");
        //this.purchaseProxyController.addService("Moshe and Avi", "some url");
        assertThrows(IllegalArgumentException.class, () ->
                this.purchaseProxyController.pay(1000, paymentParamsDTO));
    }
}
