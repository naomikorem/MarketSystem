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

public class ItemManager {

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
        im.addItem(i);
        Object i1 = im.getItem(i.getId());
        System.out.println(i1);
    }

    public Integer addItem(ItemDAL item){
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;
        Integer itemID = null;

        try {
            tx = session.beginTransaction();
            itemID = (Integer) session.save(item);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return itemID;
    }

    public Object getItem(int id) {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            ItemDAL o = session.get(ItemDAL.class, id);
            tx.commit();
            return o;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
}
