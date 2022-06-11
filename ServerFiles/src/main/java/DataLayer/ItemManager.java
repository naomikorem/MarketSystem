package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemManager extends DALManager<ItemDAL, Integer> {

    private static class ItemManagerHolder {
        static final ItemManager instance = new ItemManager();
    }

    public static ItemManager getInstance() {
        return ItemManager.ItemManagerHolder.instance;
    }

    public ItemManager() {
        super(ItemDAL.class);
    }

}
