package DomainLayer.SystemManagement.ExternalServices.SupplyServices;

import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.AbstractExternalService;

import java.util.List;
import java.util.Map;

public interface IExternalSupplyService extends AbstractExternalService
{
    boolean supply(String shipping_address, List<Map.Entry<Item, Integer>> items);
}
