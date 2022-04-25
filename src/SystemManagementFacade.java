import PurchaseServices.StubPurchaseService;
import SupplyServices.StubSupplyService;

public class SystemManagementFacade
{
    private ExternalServices services; // TODO: SINGLETON
    private PurchaseProcess purchaseProcess; // TODO: SINGLETON
    public boolean initializeMarket()
    {
        // check if there is system manager
        // create the first system admin if there is no system manager

        // check if there is supply service - same as before
        if(!services.hasPurchaseService())
        {
            services.addExternalPurchaseService(new StubPurchaseService());
        }
        // check if there is purchase service - same as before
        if(!services.hasSupplyService())
        {
            services.addExternalSupplyService(new StubSupplyService());
        }
        return true;
    }

    public boolean PurchaseShoppingCart(User user)
    {
        purchaseProcess.Pay(user);
        return true;
    }

}
