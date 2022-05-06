package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ReceiveInformationTest extends AbstractTest {

    private Store s;
    private Item i;

    public ReceiveInformationTest() {
        super();
    }

    @Before
    public void before() {
        bridge.enter();
        bridge.register("user@gmail.com", "user","first","last", "user");
        bridge.login("user", "user");
        s = bridge.addNewStore("theStore").getObject();
        i = bridge.addItemToStore(s.getStoreId(), "item1", "Food", 10, 10).getObject();
        bridge.logout();
    }

    @Test
    public void testReceiveInformation() {
        Response<Collection<Store>> r1 = bridge.getStores();
        assertFalse(r1.hadError());
        assertEquals(1, r1.getObject().size());
        Store store = r1.getObject().iterator().next();
        assertEquals(store.getStoreId(), s.getStoreId());

        Response<Map<Item, Integer>> r2 = bridge.getItems(s.getStoreId());
        assertFalse(r2.hadError());
        assertEquals(r2.getObject().size(), 1);
        assertEquals(r2.getObject().keySet().iterator().next().getId(), i.getId());
    }


}
