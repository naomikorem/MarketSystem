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

    /***
     * Interface function that all the external supply services must have.
     * The service deals with supplying the requested items to the relevant address.
     * @param shipping_address The shipping address of the user
     * @param items The items that the user paid for
     * @return true - if the supply process is successful, false - otherwise
     */
    @Override
    public boolean supply(String shipping_address, List<Map.Entry<Item, Integer>> items) {
        return true;
    }
}
