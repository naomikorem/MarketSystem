package DomainLayer;

import DomainLayer.Users.User;

public interface SystemInterface {
    public Response<User> register(String name, String password, String email);

    public Response<User> login(String name, String password);

    public void addManager(User owner, String manager, int storeId);
}
