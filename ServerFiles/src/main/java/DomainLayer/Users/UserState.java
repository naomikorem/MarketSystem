package DomainLayer.Users;

import java.util.Set;

public interface UserState {
    public String getName();
    public String getPassword();
    public String getFirstName();
    public String getLastName();
    public String getEmail();
    public boolean isSubscribed();
    public boolean login(String password);
    public void setName(String name);
    public void setEmail(String email);
    public void addManagedStore(int storeId);
    public void addOwnedStore(int storeId);
    public void removeManagedStore(int storeId);
    public void removeOwnedStore(int storeId);
    public Set<Integer> getOwnedStores();
    public Set<Integer> getManagedStores();
}
