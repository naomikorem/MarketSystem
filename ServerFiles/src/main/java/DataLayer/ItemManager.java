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

    public ItemManager() {
        super(ItemDAL.class);
    }

    public static void main(String[] args) {
        ItemDAL i = new ItemDAL();

        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("KEY1");
        keywords.add("KEY1");
        keywords.add("KEY2");
        keywords.add("KEY3");
        i.setKeyWords(keywords);
        i.setProduct_name("ITEM1");
        i.setCategory(Category.Food);
        i.setPrice(5.99);
        ItemManager im = new ItemManager();
        im.addObject(i);
        Object i1 = im.getObject(i.getId());
        System.out.println(i1);
    }
}
