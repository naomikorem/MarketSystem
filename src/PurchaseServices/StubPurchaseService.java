package PurchaseServices;

public class StubPurchaseService implements IExternalPurchaseService
{
    @Override
    public boolean pay() {
        return false;
    }
}
