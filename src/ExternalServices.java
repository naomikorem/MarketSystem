import PurchaseServices.IExternalPurchaseService;
import PurchaseServices.PurchaseProxy;
import SupplyServices.IExternalSupplyService;
import SupplyServices.StubSupplyService;
import SupplyServices.SupplyProxy;

public class ExternalServices
{
    private PurchaseProxy purchase_proxy;
    private SupplyProxy supply_proxy;

    public void addExternalPurchaseService(IExternalPurchaseService purchase_service)
    {
        purchase_proxy.addService(purchase_service);
    }

    public void addExternalSupplyService(IExternalSupplyService supply_service)
    {
        supply_proxy.addService(supply_service);
    }

    public boolean hasPurchaseService()
    {
        return purchase_proxy.hasService();
    }

    public boolean hasSupplyService()
    {
        return supply_proxy.hasService();
    }


}
