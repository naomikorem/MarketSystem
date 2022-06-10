package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ServiceDAL;
import DataLayer.DALObjects.ShoppingBasketDAL;
import DataLayer.DALObjects.UserDAL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseConnection {
    private static SessionFactory factory;

    private static Configuration getConfiguration() {
        Configuration c = new Configuration().configure("hibernate.cfg.xml");
        c.addAnnotatedClass(ItemDAL.class);
        c.addAnnotatedClass(UserDAL.class);
        c.addAnnotatedClass(ServiceDAL.class);
        c.addAnnotatedClass(ShoppingBasketDAL.class);
        return c;
    }

    public static Session getSession() {
        if (factory == null) {
            factory = getConfiguration().buildSessionFactory();
        }
        return factory.openSession();
    }
}
