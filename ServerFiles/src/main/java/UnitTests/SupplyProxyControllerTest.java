package UnitTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxy;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxyController;
import ServiceLayer.DTOs.SupplyParamsDTO;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThrows;

public class SupplyProxyControllerTest extends AbstractTest {

    private SupplyProxyController supplyProxyController;
    private List<Map.Entry<Item, Integer>> items;

    @Before
    public void setup()
    {
        this.supplyProxyController = SupplyProxyController.getInstance();

        items = new LinkedList<>();

        Item item1 = new Item("hamburger", Category.Food, 100);
        Item item2 = new Item("rice", Category.Food, 30.4);

        Map.Entry<Item, Integer> item_and_amount_1 = Map.entry(item1, 2);
        Map.Entry<Item, Integer> item_and_amount_2 = Map.entry(item2, 3);

        items.add(item_and_amount_1);
        items.add(item_and_amount_2);
    }

    @Test
    public void supplyServiceNotExists()
    {
        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                "not existing supply",
                "user",
                "user address",
                "city",
                "country",
                "777777");
        //this.supplyProxyController.addService("Moshe and Avi");
        assertThrows(IllegalArgumentException.class, () ->
                this.supplyProxyController.supply(supplyParamsDTO, items));
    }
}
