package UnitTests;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class CalculateShoppingCartTest extends AbstractTest
{
    private ShoppingBasket basket1, basket2;
    private List<ShoppingBasket> baskets;
    private Map<Item, Integer> items_basket_1, items_basket_2;
    private int store1_id;
    private int store5_id;

    public CalculateShoppingCartTest()
    {
        this.items_basket_1 = new HashMap<>();
        this.items_basket_2 = new HashMap<>();
        this.baskets = new LinkedList<>();
    }

    @Before
    public void setup()
    {
        User store_1_5_founder = new User(new SubscribedState("woner@gmail.com", "storeowners", "Sagi", "Brudni", "pass"));
        Store store1 = StoreController.getInstance().createStore(store_1_5_founder, "store1");
        Store store5 = StoreController.getInstance().createStore(store_1_5_founder, "store5");
        this.store1_id = store1.getStoreId();
        this.store5_id = store5.getStoreId();

        this.basket1 = new ShoppingBasket(store1_id);
        this.basket2 = new ShoppingBasket(store5_id);

        Item item1 = new Item("banana", Category.Food, 25);
        Item item2 = new Item("apple", Category.Food, 10);
        Item item3 = new Item("pineapple", Category.Food, 30);
        Item item4 = new Item("milk", Category.Food, 5.5);

        store1.addItem(item1, 50);
        store1.addItem(item2, 50);
        store5.addItem(item3, 50);
        store5.addItem(item4, 50);

        basket1.addItem(item1, 1);
        basket1.addItem(item2, 2);
        this.items_basket_1.put(item1, 1);
        this.items_basket_1.put(item2, 2);

        basket2.addItem(item3, 5);
        basket2.addItem(item4, 3);
        this.items_basket_2.put(item3, 5);
        this.items_basket_2.put(item4, 3);

        baskets.add(basket1);
        baskets.add(basket2);
    }

    @Test
    public void calculateRegularBasketPrice()
    {
        double actual_price = 25*1 + 10*2 + 30*5 + 5.5*3;
        double calculated_price = MarketManagementFacade.getInstance().calculateShoppingCartPrice(baskets);
        assertTrue(actual_price == calculated_price);
    }
}
