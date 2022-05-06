package DomainLayer.SystemManagement.ExternalServices;

import Utility.LogUtility;

import java.rmi.ConnectException;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxyController<T extends AbstractProxy>
{
    // Holds all the external services from specific type (purchase or supply)

    protected ConcurrentHashMap<String, T> services = new ConcurrentHashMap<>();


    protected abstract T createProxy(String name, String url) throws ConnectException; // abstract function

    /***
     * Add external service to the market system
     * @param name The name of the new external service
     */

    public synchronized void addService(String name, String url) throws ConnectException {
        if (services.containsKey(name))
        {
            LogUtility.error("tried to add a service that already exists in the system");
            throw new IllegalArgumentException("The service with the name " + name + " already exists in the system.");
        }
        services.put(name, createProxy(name, url));
        LogUtility.info("Added new external service with the name " + name);

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
        services.remove(service_name);
        LogUtility.info("Removed the external service with the name " + service_name + " from the system");

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
    }

}
