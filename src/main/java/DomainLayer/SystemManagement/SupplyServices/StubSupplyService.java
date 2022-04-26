package DomainLayer.SystemManagement.SupplyServices;

public class StubSupplyService implements IExternalSupplyService
{
    private String name;

    public StubSupplyService(String name)
    {
        this.name = name;
    }

    @Override
    public boolean supply()
    {
        return false;
    }



}
