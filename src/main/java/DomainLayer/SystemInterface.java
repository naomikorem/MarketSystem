package DomainLayer;

import DomainLayer.Users.User;

public interface SystemInterface {
    public Response<User> register(String email, String name, String password);

    public Response<User> login(String name, String password);

    public void addManager(User owner, String manager, int storeId);
}
