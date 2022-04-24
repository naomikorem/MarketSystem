package DomainLayer.Users;

import Exceptions.LogException;
import Utility.LogUtility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserController {
    private static UserController userController;

    private Map<String, User> users;
    private Set<String> loggedUsers;

    private UserController() {
        this.users = new HashMap<>();
        this.loggedUsers = new HashSet<>();
    }

    public static UserController getInstance() {
        if (userController == null) {
            userController = new UserController();
        }
        return userController;
    }

    public User createUser(String email, String name, String password) {
        if (isExist(name)) {
            throw new LogException("There is already a user with the given name", String.format("There was a failed attempt to create a user with the name %s", name));
        }
        UserState state = new SubscribedState(email, name, password);
        User u = new User(state);
        addUser(u);
        LogUtility.info(String.format("A new user named %s was created", name));
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

    public boolean isExist(String name) {
        return users.containsKey(name);
    }

    public boolean isLoggedIn(String name) {
        return loggedUsers.contains(name);
    }

    private void addLoggedUser(String name) {
        this.loggedUsers.add(name);
    }

    public User login(String name, String password) {
        if (isLoggedIn(name)) {
            throw new LogException("The user is already logged in.", String.format("There was an attempt to log in into user %s while the user was already logged in", name));
        }
        User u = getUser(name);
        if (u == null || !u.login(password)) {
            throw new LogException("One of the credentials is incorrect.", String.format("There was an attempt to log in into user %s with the wrong credentials", name));
        }
        addLoggedUser(name);
        LogUtility.info(String.format("User %s has logged in", name));
        return u;
    }

}
