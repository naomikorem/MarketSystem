package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.SimplePredicate;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class StoreManager extends DALManager<StoreDAL, Integer> {

    private static class StoreManagerHolder {
        static final StoreManager instance = new StoreManager();
    }

    public static StoreManager getInstance() {
        return StoreManager.StoreManagerHolder.instance;
    }

    public List<StoreDAL> getAllStores() {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<StoreDAL> res = session.createQuery("SELECT a FROM StoreDAL a", StoreDAL.class).getResultList();
            tx.commit();
            return res;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public StoreManager() {
        super(StoreDAL.class);
    }
}
