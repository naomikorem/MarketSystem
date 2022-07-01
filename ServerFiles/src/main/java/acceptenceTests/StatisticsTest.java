package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stats.Stats;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Store;
import DomainLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StatisticsTest extends AbstractTest {

    public StatisticsTest() {super();}

    @Before
    public void setup() {
        bridge.enter();
        bridge.register("user1@gmail.com", "user1", "first", "last", "password");
        bridge.register("user2@gmail.com", "user2", "first", "last", "password");
        bridge.register("user3@gmail.com", "user3", "first", "last", "password");

        bridge.logout();


    }

    @Test
    public void testStatsAdmin() {
        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());

    }

    @Test
    public void testStatAnotherUser() {
        assertFalse(bridge.login("user1", "password").hadError());
        bridge.logout();

        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(2, s.regularUsersCount());
        assertEquals(0, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testMultipleStatAnotherUser() {
        assertFalse(bridge.login("user1", "password").hadError());
        bridge.logout();
        assertFalse(bridge.login("user2", "password").hadError());
        bridge.logout();
        assertFalse(bridge.login("user3", "password").hadError());
        bridge.logout();

        assertFalse(bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD).hadError());
        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(4, s.regularUsersCount());
        assertEquals(0, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testStatsOwner() {
        bridge.login("user1", "password");
        bridge.addNewStore("store");

        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(1, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());

    }

    @Test
    public void testStatsManager() {
        bridge.login("user1", "password");
        Store store = bridge.addNewStore("store").getObject();
        bridge.addManager("user2", store.getStoreId());

        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(1, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());

        bridge.logout();
        bridge.login("user2", "password");
        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(1, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(1, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testStatsChangeToOwner() {
        bridge.login("user1", "password");

        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(2, s.regularUsersCount());
        assertEquals(0, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());

        bridge.logout();
        bridge.login("user1", "password");
        bridge.addNewStore("store");
        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);
        response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(1, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());

    }

    @Test
    public void testStatsChangeToManager() {
        bridge.login("user1", "password");

        bridge.logout();
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(2, s.regularUsersCount());
        assertEquals(0, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());

        Store store = bridge.addNewStore("store").getObject();
        bridge.addManager("user1", store.getStoreId());

        response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(0, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(1, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testStatsMultipleManagers() {
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Store store = bridge.addNewStore("store").getObject();
        bridge.addManager("user1", store.getStoreId());
        bridge.addManager("user2", store.getStoreId());

        bridge.logout();
        bridge.login("user1", "password");
        bridge.logout();
        bridge.login("user2", "password");
        bridge.logout();

        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(0, s.regularUsersCount());
        assertEquals(1, s.ownerUsersCount());
        assertEquals(2, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testStatsMultipleOwners() {
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Store store = bridge.addNewStore("store").getObject();
        bridge.addOwner("user1", store.getStoreId());
        bridge.addOwner("user2", store.getStoreId());

        bridge.logout();
        bridge.login("user1", "password");
        bridge.logout();
        bridge.login("user2", "password");
        bridge.logout();

        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);

        Response<List<Map.Entry<LocalDate, Stats>>> response = bridge.getStats();
        assertFalse(response.hadError());
        assertEquals(1, response.getObject().size());
        Stats s = response.getObject().get(0).getValue();
        assertEquals(1, s.adminUsersCount());
        assertEquals(0, s.regularUsersCount());
        assertEquals(3, s.ownerUsersCount());
        assertEquals(0, s.managerUsersCount());
        assertEquals(0, s.guestsCount());
    }

    @Test
    public void testStatsFail() {
        assertTrue(bridge.getStats().hadError());
        assertFalse(bridge.login("user1", "password").hadError());
        assertTrue(bridge.getStats().hadError());
    }

    @Test
    public void testStatsSuccess() {
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);
        assertFalse(bridge.getStats().hadError());
    }

}
