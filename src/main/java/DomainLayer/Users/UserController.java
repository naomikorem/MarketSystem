package main.java.DomainLayer.Users;

import java.util.Map;

public class UserController {
    private static UserController userController;

    private Map<String, User> users;

    private UserController() {}

    public UserController getInstance() {
        if (userController == null) {
            userController = new UserController();
        }
        return userController;
    }

    public User createUser(String name, String password, String email) {
        if (users.containsKey(name)) {
            throw new IllegalArgumentException("There is already a user with the give name");
        }
        UserState state = new SubscribedState(name, password, email);
        User u = new User(state);
        addUser(u);
        return u;
    }

    public void addUser(User u) {
        if (!users.containsKey(u.getName())) {
            users.put(u.getName(), u);
        }
    }

    public User getUser(String name) {
        return users.getOrDefault(name, null);
    }

}
