package DataLayer;


import DataLayer.DALObjects.NotificationDAL;
import DataLayer.DALObjects.ServiceDAL;
import org.aspectj.weaver.ast.Not;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class NotificationsManager extends DALManager<NotificationDAL, String>
{
    public NotificationsManager() {
        super(NotificationDAL.class);
    }

    public static void main(String[] args) {
        NotificationDAL i = new NotificationDAL();

        i.setMessage("hello world");
        i.setUsername("user1");
        NotificationsManager im = new NotificationsManager();
        im.addObject(i);
        List<NotificationDAL> i1 = im.getUserNotifications(i.getUsername());
        System.out.println("after get " + i1.get(0).getUsername());
        NotificationDAL b = new NotificationDAL();

        b.setUsername("user2");
        b.setMessage("hello again");
        im.addObject(b);

        //System.out.println(im.clearTable());

    }

    public List<NotificationDAL> getUserNotifications(String username)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from NotificationDAL s where s.username like ?1"), NotificationDAL.class);
            query.setParameter(1, username);
            List<NotificationDAL> notifications_by_name = query.list();
            tx.commit();
            return notifications_by_name;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public String addNotification(NotificationDAL notification){

        return super.addObject(notification);
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
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

}

