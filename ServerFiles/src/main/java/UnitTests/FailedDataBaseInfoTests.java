package UnitTests;

import DomainLayer.Stores.Store;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableRowSorter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FailedDataBaseInfoTests extends AbstractTest  {
    private static User u;
    private static Store s;

    public FailedDataBaseInfoTests() {
        super();
    }

    @Before
    public void setup() {
        u = new User(new SubscribedState("user@gmail.com", "user","first", "last", "password"));
    }

    @Test
    public void appointAnSavedUserAsManager() {
        assertTrue(u.isSubscribed());
        u.login("password");
        s = new Store(u, "store", 1);
        assertTrue(s.isOpen());
        assertTrue(s.isOwner(u));
        User manager = new User(new SubscribedState("manager@gmail.com", "manager","first", "last", "password"));
        try {
            s.addManager(u.getName(), manager);
            fail();
        }catch (Exception e) {
            assertTrue(true);
        }
        assertFalse(s.isManager(manager));
    }

    @Test
    public void appointAnSavedUserAsOwner() {
        assertTrue(u.isSubscribed());
        u.login("password");
        s = new Store(u, "store", 1);
        assertTrue(s.isOpen());
        assertTrue(s.isOwner(u));
        User owner = new User(new SubscribedState("owner@gmail.com", "owner","first", "last", "password"));
        try {
            s.addOwner(u.getName(), owner);
            fail();
        }catch (Exception e) {
            assertTrue(true);
        }
        assertFalse(s.isOwner(owner));
    }

}
