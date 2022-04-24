package DomainLayer.Users;

import Exceptions.LogException;
import Utility.Utility;

public class SubscribedState implements UserState {
    private String name;
    private String password;
    private String email;

    public SubscribedState(String email, String name, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
        checkParameters(email, name, password);
    }

    private void checkParameters(String email, String name, String password) {
        if (name == null || name.length() < 4) {
            throw new IllegalArgumentException("A user name must be at least 4 letters long.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("A password must be at least 4 letters long.");
        }
        if (email == null || !Utility.isValidEmailAddress(email)) {
            throw new IllegalArgumentException("The email is invalid.");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean login(String password) {
        return password.equals(this.password);
    }
}
