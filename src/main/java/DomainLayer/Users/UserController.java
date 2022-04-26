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

    public synchronized User createUser(String email, String name, String password) {
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

    public void removeUser(String userName){
        if(!isExist(userName)) {
            System.out.println("is unregistered");
            return;
        }
        users.remove(userName);
        if(loggedUsers.contains(userName))
            loggedUsers.remove(userName);
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

    private void removedLoggedUser(String name) {
        this.loggedUsers.remove(name);
    }

    public User login(String name, String password) {
        User u = getUser(name);
        if (u == null) {
            throw new LogException("One of the credentials is incorrect.", String.format("There was an attempt to log in into user %s with the wrong credentials", name));
        }
        synchronized (u) {
            if (isLoggedIn(name)) {
                throw new LogException("The user is already logged in.", String.format("There was an attempt to log in into user %s while the user was already logged in", name));
            }
            if (!u.login(password)) {
                throw new LogException("One of the credentials is incorrect.", String.format("There was an attempt to log in into user %s with the wrong credentials", name));
            }
            addLoggedUser(name);
            LogUtility.info(String.format("User %s has logged in", name));
            return u;
        }
    }

    public boolean logout(String name) {
        if (!isLoggedIn(name)) {
            throw new LogException("Only logged in users can log out.", "User %s has tried to log out even though they are not logged in.");
        }
        removedLoggedUser(name);
        return true;
    }

}