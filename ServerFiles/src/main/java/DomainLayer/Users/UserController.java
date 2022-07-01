package DomainLayer.Users;

import DataLayer.DALObjects.UserDAL;
import DataLayer.UserManager;
import Exceptions.LogException;
import ServiceLayer.Server;
import Utility.LogUtility;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserController {
    private static UserController userController;

    private Map<String, User> users;
    private Set<String> loggedUsers;

    private Map<String, User> tokensToUsers;
    private Map<User, String> usersToToken;

    public static String DEFAULT_ADMIN_USER = Server.prop.getProperty("defaultAdminUser", "admin");
    public static String DEFAULT_ADMIN_USER_FIRST_NAME = Server.prop.getProperty("defaultAdminFirstName", "admin");
    public static String DEFAULT_ADMIN_USER_LAST_NAME = Server.prop.getProperty("defaultAdminLastName", "admin");
    public static String DEFAULT_ADMIN_PASSWORD = Server.prop.getProperty("defaultAdminPassword", "admin");
    public static String DEFAULT_ADMIN_EMAIL = Server.prop.getProperty("defaultAdminEmail", "admin@mycompany.com");
    public static int SALT_HASH_ROUND_COUNT = 10;

    public static Lock lock = new ReentrantLock();

    public UserController() {
        this.users = new HashMap<>();
        this.loggedUsers = new HashSet<>();
        this.usersToToken = new HashMap<>();
        this.tokensToUsers = new HashMap<>();

        //load database
        loadUser(DEFAULT_ADMIN_USER);
        if (!isExist(DEFAULT_ADMIN_USER)) {
            createUser(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_FIRST_NAME, DEFAULT_ADMIN_USER_LAST_NAME, DEFAULT_ADMIN_PASSWORD);
        }
    }

    private String getToken() {
        SecureRandom secureRandom = new SecureRandom(); //threadsafe
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public void clearAll() {
        users = new HashMap<>();
        loggedUsers = new HashSet<>();

        UserManager.getInstance().clearTable();

        loadUser(DEFAULT_ADMIN_USER);
        if (!isExist(DEFAULT_ADMIN_USER)) {
            createUser(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_FIRST_NAME, DEFAULT_ADMIN_USER_LAST_NAME, DEFAULT_ADMIN_PASSWORD);
        }
    }

    public static class UserControllerHolder {
        public static final UserController instance = new UserController();
    }

    public static UserController getInstance() {
        return UserControllerHolder.instance;
    }

    public synchronized User createUser(String email, String userName, String firstName, String lastName, String password) {
        if (isExist(userName)) {
            throw new LogException("There is already a user with the given name", String.format("There was a failed attempt to create a user with the name %s", userName));
        }
        UserState state = new SubscribedState(email, userName,firstName,lastName, password);
        User u = new User(state);
        UserManager.getInstance().addObject(u.toDAL());
        addUser(u);
        LogUtility.info(String.format("A new user named %s was created", userName));
        return u;
    }

    public void addUser(User u) {
        if (!users.containsKey(u.getName())) {
            synchronized (lock) {
                users.put(u.getName(), u);
            }
        }
    }

    public void removeUser(String removedBy, String username) {
        LogUtility.info(String.format("user %s was deleted by %s", username, removedBy));
        removeUser(username);
    }

    public void removeUser(String userName) {
        synchronized (lock) {
            if (!isExist(userName)) {
                LogUtility.warn("tried to remove a nonexistent user");
                throw new IllegalArgumentException(String.format("Could not find user with name %s", userName));
            }
            User u = users.get(userName);
            UserManager.getInstance().removeObject(u.toDAL());
            users.remove(userName);
            loggedUsers.remove(userName);
            LogUtility.info(String.format("A user named %s was removed", userName));
        }
    }

    public User getUser(String name) {
        synchronized (lock)
        {
            if (isExist(name))
            {
                return users.get(name);
            }
        }
        LogUtility.warn("tried to get a nonexistent user");
        throw new IllegalArgumentException(String.format("Could not find user with name %s", name));
    }

    public synchronized void loadUser(String name) {
        if (!users.containsKey(name)) {
            UserDAL dal = UserManager.getInstance().getObject(name);
            if (dal != null) {
                User u = dal.toDomain();
                users.put(u.getName(), u);
            }
        }
    }

    public boolean isExist(String name) {
        loadUser(name);
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

    public String getToken(String name) {
        User u = getUser(name);
        if (!this.usersToToken.containsKey(u)) {
            throw new IllegalArgumentException("There is no token for the user");
        }
        return usersToToken.get(u);
    }

    public User loginUserByToken(String token) {
        token = token.replaceAll("\"", "");
        if (!this.tokensToUsers.containsKey(token)) {
            throw new IllegalArgumentException("There is no user matching to the token");
        }
        User u = tokensToUsers.get(token);
        String name = u.getName();
        synchronized (u) {
            if (isLoggedIn(name)) {
                throw new LogException("The user is already logged in.", String.format("There was an attempt to log in into user %s while the user was already logged in", name));
            }
            addLoggedUser(name);
            return u;
        }
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
            String token = getToken();
            this.tokensToUsers.put(token, u);
            this.usersToToken.put(u, token);
            LogUtility.info(String.format("User %s has logged in", name));
            return u;
        }
    }

    public boolean logout(String name) {
        if (!isLoggedIn(name)) {
            throw new LogException("Only logged in users can log out.", "User %s has tried to log out even though they are not logged in.");
        }
        removedLoggedUser(name);
        LogUtility.info(String.format("User %s has logged out", name));
        UserManager.getInstance().addObject(getUser(name).toDAL());
        return true;
    }

    public void setUserName(User user, String newName) {
        if (user.isSubscribed()) {
            synchronized (lock) {
                if (isExist(newName)) {
                    throw new IllegalArgumentException("The new user name is not unique, already exists in the system");
                }
                loggedUsers.remove(user.getName());
                users.remove(user.getName());
                LogUtility.info(String.format("user %s changed their name to %s", user.getName(), newName));
                user.setName(newName);
                users.put(newName, user);
                loggedUsers.add(newName);
            }
        }
    }

    public void setUserEmail(User user, String newEmail) {
        if (user.isSubscribed()) {
            user.setEmail(newEmail);
        }
        LogUtility.info("User "+user.getName()+" changed its email to "+newEmail);
    }
}
