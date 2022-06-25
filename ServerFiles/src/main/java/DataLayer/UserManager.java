package DataLayer;

import DataLayer.DALObjects.HistoryItemDAL;
import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ServiceDAL;
import DataLayer.DALObjects.UserDAL;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserManager extends DALManager<UserDAL, String> {

    private static class UserManagerHolder {
        static final UserManager instance = new UserManager();
    }

    public static UserManager getInstance() {
        return UserManagerHolder.instance;
    }

    public UserManager() {
        super(UserDAL.class);
    }

    public List<UserDAL> getAllUsers()
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<UserDAL> users = session.createQuery(new String("from UserDAL"), UserDAL.class).list();
            tx.commit();
            return users;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;

    }
}
