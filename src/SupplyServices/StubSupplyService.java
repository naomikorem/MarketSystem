package SupplyServices;

public class StubSupplyService implements IExternalSupplyService
{
    @Override
    public boolean supply()
    {
        return false;
    }
}
