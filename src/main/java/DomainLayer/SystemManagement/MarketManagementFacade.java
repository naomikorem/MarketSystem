package DomainLayer.SystemManagement;

import DomainLayer.Response;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.Users.User;

public class MarketManagementFacade
{
    private static class MarketManagementFacadeHolder
    {
        static final MarketManagementFacade INSTANCE = new MarketManagementFacade();
    }
    private MarketManagementFacade()
    {
        this.services = ExternalServicesHandler.getInstance();
        this.purchaseProcess = PurchaseProcess.getInstance();
    }

    // Implementation of thread safe singleton
    public static MarketManagementFacade getInstance() {
        return MarketManagementFacadeHolder.INSTANCE;
    }
    private ExternalServicesHandler services;
    private PurchaseProcess purchaseProcess;

    /***
     * The function responsible to initialize the connection with the external services, when the system is loaded
     * After this function, the system will have at least one supply service and one purchase service
     * @return Response - if the initialization succeeded or if there was an error
     */
    public Response<Boolean> initializeMarket()
    {
        try {
            // check if there is supply service - if not, add the first one
            if (!services.hasPurchaseService()) {
                services.addExternalPurchaseService("stub");
            }
            // check if there is purchase service - if not, add the first one
            if (!services.hasSupplyService()) {
                services.addExternalSupplyService("stub");
            }
            return new Response<>(true);
        }
        catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * The user wants to pay about the items in his shopping cart.
     * @param user The user that wants to pay
     * @param address The shipping address, where to send the items.
     * @param purchase_service_name Which of the external purchase services is selected for this deal.
     * @param supply_service_name Which of the external supply services is selected for this deal.
     * @return Response - if the purchase process succeeded or if there was an error
     */
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

    /***
     * A system admin can add external purchase service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public Response<Boolean> addExternalPurchaseService(String name)
    {
        try {
            this.services.addExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can remove external purchase service, if the system has more than one purchase service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public Response<Boolean> removeExternalPurchaseService(String name)
    {
        try {
            this.services.removeExternalPurchaseService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can add external supply service, if it doesn't already exist.
     * @param name The name of the new service
     * @return Response - if the addition succeeded or if there was an error
     */
    public Response<Boolean> addExternalSupplyService(String name)
    {
        try {
            this.services.addExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * A system admin can remove external supply service, if the system has more than one supply service.
     * @param name The name of the service to remove
     * @return Response - if the removal succeeded or if there was an error
     */
    public Response<Boolean> removeExternalSupplyService(String name)
    {
        try {
            this.services.removeExternalSupplyService(name);
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains purchase services - for the tests
     * @return Response - true if there is at least one purchase service, false otherwise
     */
    public Response<Boolean> hasPurchaseService()
    {
        try {
            return new Response<>(this.services.hasPurchaseService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains supply services - for the tests
     * @return Response - true if there is at least one supply service, false otherwise
     */
    public Response<Boolean> hasSupplyService()
    {
        try {
            return new Response<>(this.services.hasSupplyService());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains purchase services with the given name - for the tests
     * @return Response - true if the service was found, false otherwise
     */
    public Response<Boolean> hasPurchaseService(String purchase_service_name)
    {
        try {
            return new Response<>(this.services.hasPurchaseService(purchase_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    /***
     * Check if the system contains supply services with the given name - for the tests
     * @return Response - true if the service was found, false otherwise
     */
    public Response<Boolean> hasSupplyService(String supply_service_name)
    {
        try {
            return new Response<>(this.services.hasSupplyService(supply_service_name));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }
}
