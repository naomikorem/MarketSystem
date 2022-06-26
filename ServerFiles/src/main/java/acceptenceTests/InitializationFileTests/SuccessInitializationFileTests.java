package acceptenceTests.InitializationFileTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;
import ServiceLayer.ParseFile.Parser;
import ServiceLayer.Server;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SuccessInitializationFileTests extends AbstractTest
{
    private final String init_file = Server.INIT_FILE_PATH;
    private Parser good_file_parser;
    private final String username1 = "user1", pass1 = "pass1";
    private String username2 = "user2", pass2 = "pass2";
    private String username3 = "user3", pass3 = "pass3";
    private String username4 = "user4", pass4 = "pass4";
    private String store_name = "s";
    private Store s_store;
    private final String admin_name = "admin", admin_pass = "admin";


    @Before
    public void setup()
    {
        this.bridge.enter();
        good_file_parser = new Parser(init_file);
        good_file_parser.runCommands();
        good_file_parser.clean();
        this.bridge.login(username2, pass2);
        Response<Collection<Store>> stores = this.bridge.getUsersStores();
        this.bridge.logout();
        s_store = stores.getObject().stream().collect(Collectors.toList()).get(0);
        assertFalse(stores.hadError());
        assertEquals(stores.getObject().size(), 1);
    }

    @Test
    public void successRegistrationInitializationFile()
    {
        // check that there are 4 registered users (admin, user1, user2, user3, user4)
        this.bridge.login(admin_name, admin_pass);
        Response<User> user1 = this.bridge.getUser(username1);
        Response<User> user2 = this.bridge.getUser(username2);
        Response<User> user3 = this.bridge.getUser(username3);
        Response<User> user4 = this.bridge.getUser(username4);
        this.bridge.logout();

        assertFalse(user1.hadError());
        assertEquals(user1.getObject().getName(), username1);
        assertFalse(user2.hadError());
        assertEquals(user2.getObject().getName(), username2);
        assertFalse(user3.hadError());
        assertEquals(user3.getObject().getName(), username3);
        assertFalse(user4.hadError());
        assertEquals(user4.getObject().getName(), username4);
    }

    @Test
    public void successOpenStoreInitializationFile()
    {
        // check that the user2 opened the store s

        assertEquals(s_store.getName(), store_name);
        assertEquals(s_store.getFounder(), username2);
    }

    @Test
    public void successAddItemInitializationFile()
    {
        // check that user2 added item to store s
        String item_name = "Bamba";
        double item_price = 30;
        Integer item_amount = 20;
        this.bridge.login(username2, pass2);
        Response<Map<Item, Integer>> res = this.bridge.getItems(this.s_store.getStoreId());
        this.bridge.logout();
        Map.Entry<Item,Integer> item = res.getObject().entrySet().stream().collect(Collectors.toList()).get(0);

        assertFalse(res.hadError());
        assertEquals(res.getObject().size(), 1);
        assertEquals(item.getKey().getProductName(), item_name);
        assertTrue(item.getKey().getPrice() == item_price);
        assertEquals(item.getValue(), item_amount);
    }

    @Test
    public void successAddAdminInitializationFile()
    {
        // check that user1 is now admin
        this.bridge.login(username1, pass1);
        Response<Boolean> res = this.bridge.isLoggedInAdminCheck();
        this.bridge.logout();

        assertFalse(res.hadError());
        assertTrue(res.getObject());
    }

    @Test
    public void successAddStoreManagerInitializationFile()
    {
        // check that user3 is now the manager of store s
        this.bridge.login(username3, pass3);
        Response<Collection<Store>> res = this.bridge.getUsersStores();
        this.bridge.logout();

        assertFalse(res.hadError());
        assertTrue(res.getObject().contains(this.s_store));
    }
}
