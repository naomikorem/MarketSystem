package DomainLayer.SystemManagement.PurchaseServices;

public class StubPurchaseService implements IExternalPurchaseService
{
    private String name;

    public StubPurchaseService(String name)
    {
        this.name = name;
    }
    @Override
    public boolean pay(double amount) {
        return false;
    }
}
