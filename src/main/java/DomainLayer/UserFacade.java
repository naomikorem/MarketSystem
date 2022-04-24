package DomainLayer;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import Networking.RequestMessage;

public class UserFacade {
    private UserController userController;
    private StoreFacade storeFacade;

    public UserFacade() {
        this.userController = UserController.getInstance();
        this.storeFacade = new StoreFacade();
    }

    public Response<User> register(String email, String name, String password) {
        try {
            return new Response<>(userController.createUser(email, name, password));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<User> login(String user, String password) {
        try {
            return new Response<>(userController.login(user, password));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public boolean isExist(String user) {
        return userController.isExist(user);
    }
}
