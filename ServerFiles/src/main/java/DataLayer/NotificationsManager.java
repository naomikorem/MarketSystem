package DataLayer;


import DataLayer.DALObjects.NotificationDAL;
import DataLayer.DALObjects.NotificationsKey;
import DataLayer.DALObjects.ServiceDAL;
import org.hibernate.*;

import java.util.List;

public class NotificationsManager /* extends DALManager<NotificationDAL, String>*/
{
    // TODO: take care of implementation and extend

    private static class NotificationsManagerHolder {
        static final NotificationsManager INSTANCE = new NotificationsManager();
    }

    // Implementation of thread safe singleton
    public static NotificationsManager getInstance() {
        return NotificationsManager.NotificationsManagerHolder.INSTANCE;
    }

    private NotificationsManager() {}

    public void addNotification(NotificationDAL notification)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(notification);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<NotificationDAL> getUserNotifications(String username)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from NotificationDAL s where s.id.username like ?1"), NotificationDAL.class);
            query.setParameter(1, username);
            List<NotificationDAL> notifications_by_name = query.list();
            tx.commit();
            return notifications_by_name;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<NotificationDAL> getAllNotifications()
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<NotificationDAL> notifications = session.createQuery(new String("from NotificationDAL"), NotificationDAL.class).list();
            tx.commit();
            return notifications;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean deleteNotification(NotificationDAL notification)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(notification);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean deleteAllUserNotifications(String username)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            List<NotificationDAL> notifications = getUserNotifications(username);
            for(NotificationDAL n : notifications)
            {
                session.delete(n);
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean clearNotifications()
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.createQuery(new String("delete from NotificationDAL")).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return true;
    }
}

