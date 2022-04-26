package DomainLayer.SystemManagement.SupplyServices;

import DomainLayer.Stores.Item;

import java.util.List;

public class StubSupplyService implements IExternalSupplyService
{
    private String name;

    public StubSupplyService(String name)
    {
        this.name = name;
    }


    @Override
    public boolean supply(String shipping_address, List<Item> items) {
        return false;
    }
}
