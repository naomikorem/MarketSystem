package DomainLayer.SystemManagement.SupplyServices;

import DomainLayer.Stores.Item;

import java.util.List;
import java.util.Map;

public interface IExternalSupplyService
{
    boolean supply(String shipping_address, List<Map.Entry<Item, Integer>> items);
}
