package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;

import java.util.List;
import java.util.Map;

public class StubSupplyService implements IExternalSupplyService
{
    private String name;

    public StubSupplyService(String name)
    {
        this.name = name;
    }


    @Override
    public boolean supply(String shipping_address, List<Map.Entry<Item, Integer>> items) {
        return false;
    }
}
