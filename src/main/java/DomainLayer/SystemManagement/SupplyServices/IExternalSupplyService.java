package DomainLayer.SystemManagement.SupplyServices;

import DomainLayer.Stores.Item;

import java.util.List;

public interface IExternalSupplyService
{
    boolean supply(String shipping_address, List<Item> items);
}
