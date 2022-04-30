package UnitTests;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import acceptenceTests.AbstractTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoreTest extends AbstractTest {

    private User user;

    public StoreTest() {
        super();
    }

    @Before
    public void setup() {
        this.user = mock(User.class);
        when(user.getName()).thenReturn("user");
        when(user.isSubscribed()).thenReturn(true);
    }

    @Test
    public void testStoreFields() {
        assertThrows(IllegalArgumentException.class , () -> StoreController.getInstance().createStore(user, null));
        assertThrows(IllegalArgumentException.class , () -> StoreController.getInstance().createStore(user, ""));
        Store s = StoreController.getInstance().createStore(user, "Store");
        assertEquals(s.getFounder(), user.getName());
        assertTrue(s.isOwner(user));

        assertThrows(IllegalArgumentException.class, () -> s.setIsOpen("blabla", false));
        s.setIsOpen(user.getName(), false);
        assertFalse(s.isOpen());

        s.setIsOpen(user.getName(), true);
        assertTrue(s.isOpen());

        s.setPermanentlyClosed(true);
        assertFalse(s.isOpen());

        s.setPermanentlyClosed(false);
        assertTrue(s.isOpen());
    }

    @Test
    public void testStoreItems() {
        Item i1 = mock(Item.class);
        when(i1.hasKeyWords(List.of("Key1"))).thenReturn(true);
        when(i1.hasKeyWords(List.of("Key2"))).thenReturn(true);
        when(i1.hasKeyWords(List.of("Key1", "Key2"))).thenReturn(true);
        when(i1.isNameContains("Item")).thenReturn(true);
        when(i1.getCategory()).thenReturn(Category.Food);

        Store s = StoreController.getInstance().createStore(user, "Store");
        Whitebox.setInternalState(s, "items", Map.of(i1, 10));

        assertTrue(s.getItemsWithCategory(Category.Food).contains(i1));
        assertTrue(s.getItemsWithCategory(Category.Clothing).isEmpty());
        assertTrue(s.getItemsWithNameContains("Item").contains(i1));
        assertTrue(s.getItemsWithNameContains("Item1").isEmpty());
        assertTrue(s.getItemsWithKeyWords(List.of("Key1")).contains(i1));
        assertTrue(s.getItemsWithKeyWords(List.of("Key2")).contains(i1));
        assertTrue(s.getItemsWithKeyWords(List.of("Key1", "Key2")).contains(i1));
        assertTrue(s.getItemsWithKeyWords(List.of("Key1", "Key5")).isEmpty());
        assertTrue(s.getItemsWithKeyWords(List.of("K")).isEmpty());


    }


}
