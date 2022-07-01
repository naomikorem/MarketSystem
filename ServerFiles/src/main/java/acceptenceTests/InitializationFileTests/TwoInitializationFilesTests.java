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
import static org.junit.Assert.assertTrue;

public class TwoInitializationFilesTests extends AbstractTest{
    private final String init_file = Server.INIT_FILE_PATH;
    private final String init_file2 = "InitializationFile2.txt";
    private Parser file1_parser, file2_parser;
    private final String username1 = "user1", pass1 = "pass1";
    private String username2 = "user2", pass2 = "pass2";
    private String username3 = "user3", pass3 = "pass3";
    private String username4 = "user4", pass4 = "pass4";
    private String username5 = "user5", pass5 = "pass5";
    private String username6 = "user6", pass6 = "pass6";
    private String store_name = "s";
    private Store s_store;
    private final String admin_name = "admin", admin_pass = "admin";

    @Before
    public void setup()
    {
        file1_parser = new Parser(init_file);
        file1_parser.runCommands();
        file1_parser.clean();
        file2_parser = new Parser(init_file2);
        file2_parser.runCommands();
        file2_parser.clean();
        this.bridge.enter();
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
        // check that there are 2 new registered users (user5, user6)
        this.bridge.login(admin_name, admin_pass);
        Response<User> user1 = this.bridge.getUser(username1);
        Response<User> user2 = this.bridge.getUser(username2);
        Response<User> user3 = this.bridge.getUser(username3);
        Response<User> user4 = this.bridge.getUser(username4);
        Response<User> user5 = this.bridge.getUser(username5);
        Response<User> user6 = this.bridge.getUser(username6);
        this.bridge.logout();

        assertFalse(user1.hadError());
        assertEquals(user1.getObject().getName(), username1);
        assertFalse(user2.hadError());
        assertEquals(user2.getObject().getName(), username2);
        assertFalse(user3.hadError());
        assertEquals(user3.getObject().getName(), username3);
        assertFalse(user4.hadError());
        assertEquals(user4.getObject().getName(), username4);
        assertFalse(user5.hadError());
        assertEquals(user5.getObject().getName(), username5);
        assertFalse(user6.hadError());
        assertEquals(user6.getObject().getName(), username6);
    }

    @Test
    public void successUpdateItemInitializationFile()
    {
        // check that user2 updated the item amount to 10
        String item_name = "Bamba";
        double item_price = 30;
        Integer item_amount = 10;
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
    public void successAddStoreOwnersInitializationFile()
    {
        // check that user4 is now the owner of store s
        this.bridge.login(username4, pass4);
        Response<Collection<Store>> res4 = this.bridge.getUsersStores();
        this.bridge.logout();

        assertFalse(res4.hadError());
        assertTrue(res4.getObject().contains(this.s_store));

        // check that user4 is now the owner of store s
        this.bridge.login(username5, pass5);
        Response<Collection<Store>> res5 = this.bridge.getUsersStores();
        this.bridge.logout();

        assertFalse(res5.hadError());
        assertTrue(res5.getObject().contains(this.s_store));
    }
}

