package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class DALManager <T extends DALObject<K>, K> {
    private Class<? extends DALObject<K>> type;

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
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
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
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
