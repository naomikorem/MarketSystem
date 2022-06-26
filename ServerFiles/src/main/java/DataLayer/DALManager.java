package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.StatisticsDAL;
import DataLayer.DALObjects.StoreDAL;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DALManager <T extends DALObject<K>, K> {
    private Class<T> type;

    public DALManager(Class<T> type) {
        this.type = type;
    }

    public K addObject(T o){
        K id = null;
        if (!Server.useDB) {
            try {
                id = type.newInstance().getId();
            } catch (Exception e) {
                id = null;
            }
            if (id instanceof Integer) {
                id = (K) ((Integer) (new Random()).nextInt());
            }
            return id;
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(o);
            id = o.getId();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return id;
    }

    public T getObject(K id) {
        if (!Server.useDB) {
            return null;
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            T o = (T) session.get(type, (Serializable) id);
            tx.commit();
            return o;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

    public boolean clearTable()
    {
        if (!Server.useDB) {
            return true;
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.createQuery(new String("delete from " + type.getName())).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
        return true;
    }

    public void removeObject(T o){
        if (!Server.useDB) {
            return;
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                throw new RuntimeException("The service is currently unavailable - No connection to database");
            }
        }
    }

    public List<T> getAllObjects() {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.out.println(type.getName());
            System.out.println(type.getSimpleName());
            List<T> res = session.createQuery(String.format("SELECT a FROM %s a", type.getSimpleName()), type).getResultList();
            tx.commit();
            return res;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }
}
