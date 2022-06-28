package DataLayer;

import DataLayer.DALObjects.ServiceDAL;
import ServiceLayer.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ServicesManager extends DALManager<ServiceDAL, Integer>
{
    private static class ServicesManagerHolder {
        static final ServicesManager INSTANCE = new ServicesManager();
    }

    private ServicesManager() {
        super(ServiceDAL.class);
    }

    // Implementation of thread safe singleton
    public static ServicesManager getInstance() {
        return ServicesManager.ServicesManagerHolder.INSTANCE;
    }

    public Integer addService(ServiceDAL service){

        return super.addObject(service);
    }

    public List<ServiceDAL> getServicesByName(String service_name)
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from ServiceDAL s where s.name like ?1"), ServiceDAL.class);
            query.setParameter(1, service_name);
            List<ServiceDAL> services_by_name = query.list();
            tx.commit();
            return services_by_name;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

    public List<ServiceDAL> getAllServices()
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        return super.getAllObjects();
    }

    public List<ServiceDAL> getAllServicesByType(ServiceType service_type)
    {
        if (!Server.useDB) {
            return new ArrayList<>();
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(new String("select s from ServiceDAL s where s.service_type like ?1"), ServiceDAL.class);
            query.setParameter(1, service_type);
            List<ServiceDAL> services_by_type = query.list();
            tx.commit();
            return services_by_type;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
    }

    public List<ServiceDAL> getAllPurchaseServices()
    {
        return getAllServicesByType(ServiceType.Purchase);
    }

    public List<ServiceDAL> getAllSupplyServices()
    {
        return getAllServicesByType(ServiceType.Supply);
    }

    public boolean deleteAllServicesByName(String service_name)
    {
        if (!Server.useDB) {
            return true;
        }
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            List<ServiceDAL> services = getServicesByName(service_name);
            for(ServiceDAL service : services)
            {
                session.delete(service);
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        } finally {
            session.close();
        }
        return true;
    }

    public boolean clearServices()
    {
        if (!Server.useDB) {
            return true;
        }
        return super.clearTable();
    }
}
