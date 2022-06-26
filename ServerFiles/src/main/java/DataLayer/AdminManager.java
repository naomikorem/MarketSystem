package DataLayer;

import DataLayer.DALObjects.AdminDAL;
import DataLayer.DALObjects.ServiceDAL;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.ArrayList;
import java.util.List;

public class AdminManager extends DALManager<AdminDAL, String>
{
    private static class AdminManagerHolder {
        static final AdminManager INSTANCE = new AdminManager();
    }

    private AdminManager() {
        super(AdminDAL.class);
    }

    // Implementation of thread safe singleton
    public static AdminManager getInstance() {
        return AdminManager.AdminManagerHolder.INSTANCE;
    }

    public List<AdminDAL> loadAllSystemAdmins()
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<AdminDAL> admins = session.createQuery(new String("from AdminDAL"), AdminDAL.class).list();
            tx.commit();
            return admins;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

}
