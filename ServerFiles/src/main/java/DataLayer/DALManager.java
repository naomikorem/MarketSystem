package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;

public class DALManager <T, K> {
    private Class<T> type;

    public DALManager(Class<T> type) {
        this.type = type;
    }
    public K addObject(T o){
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;
        K id = null;
        try {
            tx = session.beginTransaction();
            id = (K) session.save(o);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public T getObject(K id) {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            T o = session.get(type, (Serializable) id);
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
