package DataLayer;

import DataLayer.DALObjects.HistoryItemDAL;
import DataLayer.DALObjects.ServiceDAL;
import DomainLayer.Stores.Category;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryManager extends DALManager<HistoryItemDAL, Integer>
{
    private static class HistoryManagerHolder {
        static final HistoryManager INSTANCE = new HistoryManager();
    }

    private HistoryManager() {
        super(HistoryItemDAL.class);
    }

    // Implementation of thread safe singleton
    public static HistoryManager getInstance() {
        return HistoryManager.HistoryManagerHolder.INSTANCE;
    }

    public Integer addItemToHistory(HistoryItemDAL item)
    {
        Integer res = super.addObject(item);
        if(res == null)
            return -1;
        else return res;
    }

    public List<HistoryItemDAL> getUserHistoryItems(String username)
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
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
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

    public List<HistoryItemDAL> getStoreHistoryItems(Integer store_id)
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
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
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

    public List<HistoryItemDAL> getAllHistoryItems()
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }

        return super.getAllObjects();
    }
}
