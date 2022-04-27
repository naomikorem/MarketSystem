package DomainLayer.SystemManagement;

import DomainLayer.Response;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.Users.User;

public class MarketManagementFacade
{
    private static class MarketManagementFacadeHolder {
        static final MarketManagementFacade INSTANCE = new MarketManagementFacade();
    }
    private MarketManagementFacade()
    {
        this.services = ExternalServicesHandler.getInstance();
        this.purchaseProcess = PurchaseProcess.getInstance();
    }

    public static MarketManagementFacade getInstance() {
        return MarketManagementFacadeHolder.INSTANCE;
    }
    private ExternalServicesHandler services;
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

    public Response<Boolean> purchaseShoppingCart(User user, String address, String purchase_service_name, String supply_service_name)
    {
        try {
            purchaseProcess.handlePurchase(user, address, purchase_service_name, supply_service_name);
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

    public Response<Boolean> hasPurchaseService()
    {
        try {
            return new Response<>(this.services.hasPurchaseService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> hasSupplyService()
    {
        try {
            return new Response<>(this.services.hasSupplyService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }


    public Response<Boolean> hasPurchaseService(String purchase_service_name)
    {
        try {
            return new Response<>(this.services.hasPurchaseService(purchase_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> hasSupplyService(String supply_service_name)
    {
        try {
            return new Response<>(this.services.hasSupplyService(supply_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

}
