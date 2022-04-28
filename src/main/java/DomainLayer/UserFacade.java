package DomainLayer;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;

public class UserFacade {
    private UserController userController;
    private AdminFacade adminFacade;

    public UserFacade() {
        this.userController = UserController.getInstance();
        this.adminFacade = new AdminFacade();
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

    public Response<Boolean> logout(String user) {
        try {
            return new Response<>(userController.logout(user));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public User getUser(String username) {
        try {
            return new Response<>(userController.getUser(username));
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public boolean isExist(String user) {
        return userController.isExist(user);
    }

    public Response<Boolean> addAdmin(String name) {
        if (isExist(name)) {
            return adminFacade.addAdmin(name);
        } else {
            return new Response<>(String.format("There is no user with the name %s", name));
        }
    }

    public Response<Boolean> isAdmin(String name) {
        return adminFacade.isAdmin(name);
    }

    public Response<Boolean> removeAdmin(String name) {
        return adminFacade.removeAdmin(name);
    }
}
