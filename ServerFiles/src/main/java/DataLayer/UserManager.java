package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.UserDAL;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
