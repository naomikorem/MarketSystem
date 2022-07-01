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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class FailInitializationFileTests extends AbstractTest
{
    private final String bad_init_file = Server.BAD_INIT_FILE_PATH, non_existing_init_file = "non_existing_init_file.txt";
    private Parser bad_file_parser, non_existing_file_parser;
    private final String username1 = "user1", pass1 = "pass1";
    private String username2 = "user2", pass2 = "pass2";
    private String username3 = "user3", pass3 = "pass3";
    private String username4 = "user4", pass4 = "pass4";
    private final String admin_name = "admin", admin_pass = "admin";

    @Before
    public void setup()
    {
        this.bridge.enter();
        bad_file_parser = new Parser(bad_init_file);
        bad_file_parser.runCommands();
        non_existing_file_parser = new Parser(non_existing_init_file);
        non_existing_file_parser.runCommands();
    }

    @Test
    public void failureRegistrationInitializationFile()
    {
        // check that there are no registered users from the good initialization file
        Response<User> user1 = this.bridge.getUser(username1);
        Response<User> user2 = this.bridge.getUser(username2);
        Response<User> user3 = this.bridge.getUser(username3);
        Response<User> user4 = this.bridge.getUser(username4);
        this.bridge.logout();

        assertTrue(user1.hadError());
        assertTrue(user2.hadError());
        assertTrue(user3.hadError());
        assertTrue(user4.hadError());
    }

    @Test
    public void failureOpenStoreInitializationFile()
    {
        // check that there are no stores - store s doesn't exist
        Response<Collection<Store>> stores_in_db = this.bridge.getStores();
        assertFalse(stores_in_db.hadError());
        assertEquals(stores_in_db.getObject().size(), 0);
    }

    @Test
    public void failureAddItemInitializationFile()
    {
        String item_name = "Bamba";
        Response<Collection<Store>> stores = this.bridge.getStores();
        List<Set<Item>> all_items_in_db = stores.getObject().stream().map(s-> s.getItems().keySet()).collect(Collectors.toList());
        List<Set<Item>> matching_to_name = all_items_in_db.stream().filter(s -> s.stream().filter(item -> item.getProductName().equals(item_name)).collect(Collectors.toList()).size() != 0).collect(Collectors.toList());

        assertFalse(stores.hadError());
        assertEquals(matching_to_name.size(), 0);
    }

    @Test
    public void failureAddAdminInitializationFile()
    {
        // check that user1 is now admin
        this.bridge.login(username1, pass1);
        Response<Boolean> res = this.bridge.isLoggedInAdminCheck();
        this.bridge.logout();

        assertTrue(res.hadError());
    }
}
