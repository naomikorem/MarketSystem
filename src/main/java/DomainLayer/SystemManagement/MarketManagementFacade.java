package DomainLayer.SystemManagement;

import DomainLayer.AdminFacade;
import DomainLayer.Response;
import DomainLayer.StoreFacade;
import DomainLayer.SystemManagement.NotificationManager.NotificationFacade;
import DomainLayer.SystemManagement.PurchaseServices.StubPurchaseService;
import DomainLayer.SystemManagement.SupplyServices.StubSupplyService;
import DomainLayer.Users.User;

public class MarketManagementFacade
{
    private static class MarketManagementFacadeHolder {
        static final MarketManagementFacade INSTANCE = new MarketManagementFacade();
    }
    private MarketManagementFacade()
    {
    }

    public static MarketManagementFacade getInstance() {
        return MarketManagementFacadeHolder.INSTANCE;
    }
    private ExternalServices services;
    private PurchaseProcess purchaseProcess;

    public Response<Boolean> initializeMarket()
    {
        try {

            // check if there is supply service - same as before
            if (!services.hasPurchaseService()) {
                services.addExternalPurchaseService("stub");
            }
            // check if there is purchase service - same as before
            if (!services.hasSupplyService()) {
                services.addExternalSupplyService("stub");
            }
            return new Response<>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> PurchaseShoppingCart(User user)
    {
        try {
            purchaseProcess.Pay(user, "stub", "stub"); // TODO: change to actual input
            return new Response<>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addExternalPurchaseService(String name)
    {
        try {
            this.services.addExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeExternalPurchaseService(String name)
    {
        try {
            this.services.removeExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> addExternalSupplyService(String name)
    {
        try {
            this.services.addExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> removeExternalSupplyService(String name)
    {
        try {
            this.services.removeExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

}
