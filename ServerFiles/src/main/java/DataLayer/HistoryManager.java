package DataLayer;

import DataLayer.DALObjects.HistoryItemDAL;
import DataLayer.DALObjects.ServiceDAL;
import DomainLayer.Stores.Category;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class HistoryManager extends DALManager<HistoryItemDAL, Integer>
{
    public HistoryManager() {
        super(HistoryItemDAL.class);
    }

    public static void main(String[] args) {
        HistoryItemDAL i = new HistoryItemDAL();
        i.setAmount(3);
        i.setCategory(Category.Food);
        i.setPrice_per_unit(5.5);
        i.setProduct_name("banana");
        i.setStore_id(1);
        i.setUsername("user1");
        i.setDate(new Date());

        HistoryItemDAL i1 = new HistoryItemDAL();
        i1.setAmount(11);
        i1.setCategory(Category.Clothing);
        i1.setPrice_per_unit(8.5);
        i1.setProduct_name("banana");
        i1.setStore_id(1);
        i1.setUsername("user2");
        i1.setDate(new Date());

        HistoryManager im = new HistoryManager();
        im.addItemToHistory(i);
        im.addItemToHistory(i1);

        List<HistoryItemDAL> list = im.getStoreHistoryItems(2);
        System.out.println(list);
    }

    public Integer addItemToHistory(HistoryItemDAL item)
    {
        return super.addObject(item);
    }

    public List<HistoryItemDAL> getUserHistoryItems(String username)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from HistoryItemDAL s where s.username like ?1"), HistoryItemDAL.class);
            query.setParameter(1, username);
            List<HistoryItemDAL> history_items_by_name = query.list();
            tx.commit();
            return history_items_by_name;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<HistoryItemDAL> getStoreHistoryItems(Integer store_id)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from HistoryItemDAL s where s.store_id like ?1"), HistoryItemDAL.class);
            query.setParameter(1, store_id);
            List<HistoryItemDAL> history_items_by_store = query.list();
            tx.commit();
            return history_items_by_store;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
}
