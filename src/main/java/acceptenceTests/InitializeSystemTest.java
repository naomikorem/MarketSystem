package acceptenceTests;

import DomainLayer.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InitializeSystemTest extends AbstractTest {

    public InitializeSystemTest() {
        super();
    }

    @Before
    public void setup(){
        bridge.enter();
    }

    @Test
    public void testInitializeMarket()
    {
        Response<Boolean> has_purchase_service = bridge.hasPurchaseService();
        assertFalse(has_purchase_service.hadError());
        assertTrue(has_purchase_service.getObject());

        Response<Boolean> has_supply_service = bridge.hasSupplyService();
        assertFalse(has_supply_service.hadError());
        assertTrue(has_supply_service.getObject());

        Response<Boolean> has_admin = bridge.hasAdmin();
        assertFalse(has_admin.hadError());
        assertTrue(has_admin.getObject());
    }

}
