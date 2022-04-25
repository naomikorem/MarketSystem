package DomainLayer;

import DomainLayer.Stores.Store;
import DomainLayer.Users.User;

import java.util.Collection;

public interface SystemInterface {
    public Response<Boolean> enter();

    public Response<Boolean> exit();

    public Response<User> register(String email, String name, String password);

    public Response<User> login(String name, String password);

    public void addManager(User owner, String manager, int storeId);

    public Response<Collection<Store>> getAllStores();

    public Response<Store> getStore(int id);
}
