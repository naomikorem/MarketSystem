package acceptenceTests;


import DomainLayer.Response;
import DomainLayer.Stores.Store;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.Assert.*;

public class StoreInformationTest extends AbstractTest {

    private int storeId;
    private String storeName;
    private static boolean isInit = false;

    public StoreInformationTest() {
        super();
    }

    @Before
    public void setup() {
        if (!isInit) {
            isInit = true;
            bridge.enter();
            bridge.register("user111@gmail.com", "user", "password");
            bridge.login("user", "password");
            this.storeName = "MyStore";
            this.storeId = bridge.addNewStore(storeName).getObject().getStoreId();
        }
    }

    @Test
    public void testStoreInformationSuccess() {
        Response<Store> res = bridge.getStoreInformation(storeId);
        assertFalse(res.hadError());
        assertEquals(res.getObject().getName(), this.storeName);
        assertEquals(res.getObject().getStoreId(), this.storeId);
    }

    @Test
    public void testStoreInformationFailure() {
        assertTrue(bridge.getStoreInformation(-1).hadError());
        assertTrue(bridge.getStoreInformation(storeId++).hadError());
    }


}
