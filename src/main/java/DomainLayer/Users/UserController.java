package DomainLayer.Users;

import java.util.Map;

public class UserController {
    private static UserController userController;

    private int NEXT_USER_ID = 1;
    private Map<Integer, User> users;

    private UserController() {}

    public UserController getInstance() {
        if (userController == null) {
            userController = new UserController();
        }
        return userController;
    }

    private int getNewUserId() {
        return NEXT_USER_ID++;
    }

    public User createUser(String name, String password, String email) {
        UserState state = new SubscribedState(getNewUserId(), name, password, email);
        User u = new User(state);
        addUser(u);
        return u;
    }

    public void addUser(User u) {
        if (!users.containsKey(u.getId())) {
            users.put(u.getId(), u);
        }
    }

    public User getUser(int id) {
        return users.getOrDefault(id, null);
    }

}
