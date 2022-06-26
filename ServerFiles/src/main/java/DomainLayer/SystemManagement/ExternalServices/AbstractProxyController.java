package DomainLayer.SystemManagement.ExternalServices;

import DataLayer.DALObjects.ServiceDAL;
import DataLayer.ServicesManager;
import Utility.LogUtility;
import lombok.SneakyThrows;

import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxyController<T extends AbstractProxy>
{
    // Holds all the external services from specific type (purchase or supply)
    protected ConcurrentHashMap<String, T> services = new ConcurrentHashMap<>();
    protected ServicesManager manager = ServicesManager.getInstance();

    protected abstract T createProxy(String name, String url) throws ConnectException; // abstract function
    protected abstract ServiceDAL toDAL(String name, String url);
    public abstract void loadAllServices();

    /***
     * Add external service to the market system
     * @param name The name of the new external service
     * @param url The connection with the external service
     */
    public synchronized void addService(String name, String url) throws ConnectException {
        if (services.containsKey(name))
        {
            LogUtility.error("tried to add a service that already exists in the system");
            throw new IllegalArgumentException("The service with the name " + name + " already exists in the system.");
        }
        services.put(name, createProxy(name, url));
        LogUtility.info("Added new external service with the name " + name);
        if(manager.addService(toDAL(name, url)) == null)
        {
            services.remove(name);
            throw new RuntimeException("Could not add the service" + name + " to database");
        };
    }

    /***
     * Remove external service from the market system
     * @param service_name The name of the new external service
     */
    public synchronized void removeService(String service_name)
    {
        if (!services.containsKey(service_name))
        {
            LogUtility.error("tried to remove external service that does not exists in the system");
            throw new IllegalArgumentException("The service with the name " + service_name + " does not exists in the system.");
        }
        if (services.size() == 1)
        {
            LogUtility.error("tried to remove the last external service from this kind in the system");
            throw new IllegalArgumentException("cannot remove the service " + service_name + " because it is the last connection to service in the system from this category.");
        }

        T temp_proxy = services.get(service_name);
        services.remove(service_name);
        LogUtility.info("Removed the external service with the name " + service_name + " from the system");
        if(!manager.deleteAllServicesByName(service_name))
        {
            // return to state when all was good
            services.put(service_name, temp_proxy);
            throw new RuntimeException("The service is currently unavailable - No connection to database");
        }
    }

    public synchronized List<String> getAllExternalServicesNames()
    {
        if (services.isEmpty())
        {
            LogUtility.error("tried to get external services, but services map is empty");
            throw new IllegalArgumentException("There are zero external services connected to the system.");
        }
        return new ArrayList<>(services.keySet());
    }

    /***
     * Check if the system contains at least one external service from this kind.
     * @return true or false
     */
    public boolean hasService()
    {
        return !services.isEmpty();
    }

    /***
     * Check if specific external service exists in the system by its name
     * @param service_name The name of the requested service
     * @return true or false
     */
    public boolean hasService(String service_name)
    {
        return services.containsKey(service_name);
    }

    public void clearServices()
    {
        this.services = new ConcurrentHashMap<>();
        this.manager.clearServices();
    }

    @SneakyThrows
    protected T toDomain(ServiceDAL s)
    {
        return createProxy(s.getName(), s.getUrl());
    }
}
