package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

public class StubPurchaseService implements IExternalPurchaseService
{
    private String name;

    public StubPurchaseService(String name)
    {
        this.name = name;
    }

    /***
     * Interface function that all the external purchase services must have.
     * The service deals with user's payment using the relevant details (like creditcard).
     * @param amount The amount to pay
     * @return true - if the payment process is successful, false - otherwise
     */
    @Override
    public boolean pay(double amount) {
        return true;
    }
}
