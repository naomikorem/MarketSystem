package DataLayer;

import DataLayer.DALObjects.*;
import ServiceLayer.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class DatabaseConnection {
    private static SessionFactory factory;

    private static Configuration getConfiguration() {
        Configuration c = new Configuration().configure();
        Properties properties = Server.prop;
        c.addProperties(properties);

        c.addAnnotatedClass(ItemDAL.class);
        c.addAnnotatedClass(UserDAL.class);
        c.addAnnotatedClass(ServiceDAL.class);
        c.addAnnotatedClass(ShoppingBasketDAL.class);
        c.addAnnotatedClass(NotificationsKey.class);
        c.addAnnotatedClass(NotificationDAL.class);
        c.addAnnotatedClass(HistoryItemDAL.class);
        c.addAnnotatedClass(PredicateDAL.class);
        c.addAnnotatedClass(CompositePredicateDAL.class);
        c.addAnnotatedClass(SimplePredicateDAL.class);
        c.addAnnotatedClass(DiscountDAL.class);
        c.addAnnotatedClass(SimpleDiscountDAL.class);
        c.addAnnotatedClass(CompositeDiscountDAL.class);
        c.addAnnotatedClass(PurchasePolicyDAL.class);
        c.addAnnotatedClass(SimplePurchasePolicyDAL.class);
        c.addAnnotatedClass(CompositePurchasePolicyDAL.class);
        c.addAnnotatedClass(StoreDAL.class);
        c.addAnnotatedClass(PermissionDAL.class);
        c.addAnnotatedClass(AdminDAL.class);
        c.addAnnotatedClass(StatisticsDAL.class);
        return c;
    }

    public static Session getSession() {
        if (factory == null) {
            factory = getConfiguration().buildSessionFactory();
        }
        return factory.openSession();
    }
}
