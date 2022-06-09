package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ServiceDAL;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ServicesManager
{
    public static int PURCHASE_TYPE = 1;
    public static int SUPPLY_TYPE = 2;

    public boolean addService(ServiceDAL item){
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(item);
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

    public boolean deleteService(String service_name)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            ServiceDAL service;
            do {
                service = (ServiceDAL)session.get(ServiceDAL.class, service_name);
                session.delete(service);
            }
            while (service != null);
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

    public boolean clearServices()
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.createQuery(new String("DELETE FROM Services")).executeUpdate();
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

    public List<ServiceDAL> getServicesByName(String service_name)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<ServiceDAL> services_by_name = session.createQuery("FROM Services s WHERE s.Name=" + service_name, ServiceDAL.class).list();
            tx.commit();
            return services_by_name;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<ServiceDAL> getAllServices(int service_type)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<ServiceDAL> services = session.createQuery(new String("FROM Services"), ServiceDAL.class).list();
            tx.commit();
            return services;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<ServiceDAL> getAllServicesFromType(int service_type)
    {
        Session session = DatabaseConnection.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<ServiceDAL> services = session.createQuery(new String("FROM Services s WHERE s.Type=" + service_type), ServiceDAL.class).list();
            tx.commit();
            return services;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List<ServiceDAL> getAllPurchaseServices()
    {
        return getAllServicesFromType(PURCHASE_TYPE);
    }

    public List<ServiceDAL> getAllSupplyServices()
    {
        return getAllServicesFromType(SUPPLY_TYPE);
    }
}
