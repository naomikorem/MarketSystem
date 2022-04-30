package DomainLayer.Users;

import java.util.Set;

public class GuestState implements UserState {
    @Override
    public String getName() {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public String getEmail() {
        throw new RuntimeException("Guest user does not have an email field");
    }

    @Override
    public boolean isSubscribed() {
        return false;
    }

    @Override
    public boolean login(String password) {
        return false;
    }

    @Override
    public void setName(String name) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void setEmail(String email) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void addManagedStore(int storeId) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void addOwnedStore(int storeId) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void removeManagedStore(int storeId) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void removeOwnedStore(int storeId) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public Set<Integer> getOwnedStores() {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public Set<Integer> getManagedStores() {
        throw new RuntimeException("Guest user does not have a name field");
    }
}
