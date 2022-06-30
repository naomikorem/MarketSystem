package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.StoreFacade;
import DomainLayer.Stores.OwnerAgreement;
import DomainLayer.Stores.Store;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OwnerAgreementTests extends AbstractTest {
    private Store s;

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user@gmail.com", "user", "user", "user", "user");
        bridge.register("user@gmail.com", "user2", "user", "user", "user2");
        bridge.login("admin", "admin");
        this.s = bridge.addNewStore("mystore").getObject();
    }

    @Test
    public void acceptSingle() {
        assertFalse(bridge.addOwnerAgreement("user", s.getStoreId()).hadError());
        List<String> owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertFalse(owners.contains("user"));
        Response<OwnerAgreement> r = bridge.approveOwnerAgreement(s.getStoreId(), "user");
        assertFalse(r.hadError());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertTrue(owners.contains("user"));
    }

    @Test
    public void acceptMultiple() {

        assertFalse(bridge.addOwnerAgreement("user", s.getStoreId()).hadError());
        List<String> owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertFalse(owners.contains("user"));
        Response<OwnerAgreement> r = bridge.approveOwnerAgreement(s.getStoreId(), "user");
        assertFalse(r.hadError());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertTrue(owners.contains("user"));

        assertFalse(bridge.addOwnerAgreement("user2", s.getStoreId()).hadError());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertFalse(owners.contains("user2"));
        r = bridge.approveOwnerAgreement(s.getStoreId(), "user2");
        assertFalse(r.hadError());
        bridge.logout();
        bridge.login("user", "user");
        bridge.approveOwnerAgreement(s.getStoreId(), "user2");
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertTrue(owners.contains("user2"));

    }

    @Test
    public void acceptWithRemovedOwner() {

        assertFalse(bridge.addOwnerAgreement("user", s.getStoreId()).hadError());
        List<String> owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertFalse(owners.contains("user"));
        Response<OwnerAgreement> r = bridge.approveOwnerAgreement(s.getStoreId(), "user");
        assertFalse(r.hadError());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertTrue(owners.contains("user"));

        assertFalse(bridge.addOwnerAgreement("user2", s.getStoreId()).hadError());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertFalse(owners.contains("user2"));
        r = bridge.approveOwnerAgreement(s.getStoreId(), "user2");
        assertFalse(r.hadError());
        bridge.removeOwner("user", s.getStoreId());
        owners = bridge.getStoreOwners(s.getStoreId()).getObject();
        assertTrue(owners.contains("user2"));

    }

    @Test
    public void badCase() {
        assertTrue(bridge.addOwnerAgreement("user4", s.getStoreId()).hadError());
    }
}
