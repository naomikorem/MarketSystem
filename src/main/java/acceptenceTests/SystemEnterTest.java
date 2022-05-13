package acceptenceTests;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SystemEnterTest extends AbstractTest {

    public SystemEnterTest() {
        super();
    }

    @Test
    public void testEnter() {
        assertFalse(bridge.enter().hadError());
        assertFalse(bridge.getShoppingCartItems().hadError());
        assertTrue(bridge.getShoppingCartItems().getObject().isEmpty());
    }

}
