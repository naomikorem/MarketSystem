package DataLayer;


import DataLayer.DALObjects.NotificationDAL;
import DataLayer.DALObjects.NotificationsKey;
import DataLayer.DALObjects.ServiceDAL;
import org.hibernate.*;

import java.util.List;

public class NotificationsManager /* extends DALManager<NotificationDAL, String>*/
{
    // TODO: take care of implementation and extend
    // add clear table function

    private static class NotificationsManagerHolder {
        static final NotificationsManager INSTANCE = new NotificationsManager();
    }

    // Implementation of thread safe singleton
    public static NotificationsManager getInstance() {
        return NotificationsManager.NotificationsManagerHolder.INSTANCE;
    }

    private NotificationsManager() {
       // super(NotificationDAL.class);
    }

    /*public static void main(String[] args) {
        NotificationsKey k = new NotificationsKey("user1","hello world6");
        NotificationDAL i = new NotificationDAL(k);
        NotificationsManager im = new NotificationsManager();
        im.addNotification(i);
        List<NotificationDAL> i1 = im.getUserNotifications(i.getId().getUsername());
        System.out.println("after get " + i1.get(0).getId().getUsername());

        NotificationsKey k1 = new NotificationsKey("user2","hello again6");
        NotificationDAL b = new NotificationDAL(k1);
        im.addNotification(b);

        //im.deleteNotification(i);
        //System.out.println(im.deleteAllUserNotifications("user1"));

    }*/

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
            //e.printStackTrace();
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
            //e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    /*public String addNotification(NotificationDAL notification){

        return super.addObject(notification);
    }*/

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
//            e.printStackTrace();
//            return false;
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
//            e.printStackTrace();
//            return false;
        } finally {
            session.close();
        }
        return true;
    }

}

