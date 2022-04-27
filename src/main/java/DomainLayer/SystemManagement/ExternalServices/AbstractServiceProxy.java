package DomainLayer.SystemManagement.ExternalServices;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServiceProxy<T extends AbstractExternalService>
{
    protected final ConcurrentHashMap<String, T> services = new ConcurrentHashMap<String, T>();

    protected abstract T ServiceFactory(String name);

    public void addService(String name)
    {
        synchronized (services) {
            if (services.containsKey(name))
                throw new IllegalArgumentException("The service with the name " + name + " already exists in the system.");
            services.put(name, ServiceFactory(name));
        }
    }

    public void removeService(String service_name)
    {
        synchronized (services) {
            if (services.size() == 1)
            {
                throw new IllegalArgumentException("cannot remove the service " + service_name + " because it is the last connection to service in the system from this category.");
            }

            if (!services.containsKey(service_name))
                throw new IllegalArgumentException("The service with the name " + service_name + " does not exists in the system.");

            services.remove(service_name);
        }
    }

    public boolean hasService()
    {
        return !services.isEmpty();
    }

    public boolean hasService(String service_name)
    {
        return services.containsKey(service_name);
    }
}
