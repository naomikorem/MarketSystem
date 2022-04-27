package DomainLayer.Users;

import Exceptions.LogException;
import Utility.Utility;
import Utility.LogUtility;

import java.util.regex.Pattern;

public class SubscribedState implements UserState {
    private static final int MAX_NAME_LENGTH = 25;
    private static final int MIN_NAME_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 25;
    private static final int MIN_PASSWORD_LENGTH = 4;

    private String name;
    private String password;
    private String email;

    public SubscribedState(String email, String name, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
        checkParameters(name, password, email);
    }

    public static boolean isValidUsername(String name) {
        return name != null && name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH && Pattern.matches("^[a-zA-Z0-9]+$", name);
    }

    private void checkParameters(String name, String password, String email) {
        if (!isValidUsername(name)) {
            throw new IllegalArgumentException("A user name must be at least 4 letters long.");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("A password must be at least 4 letters long.");
        }
        if (email == null || !Utility.isValidEmailAddress(email)) {
            throw new IllegalArgumentException("The email is invalid.");
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String userName){
        if (!isValidUsername(userName)) {
            throw new IllegalArgumentException("A user name must be at least 4 letters long.");
        }
        String oldName = this.name;
        this.name = userName;
        LogUtility.info(String.format("User %s changed its name to %s", oldName, this.name));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail){
        if (newEmail == null || !Utility.isValidEmailAddress(newEmail)) {
            throw new IllegalArgumentException("The email is invalid.");
        }
        this.email = newEmail;
        LogUtility.info(String.format("User %s changed its email to %s", this.name, this.email));
    }

    @Override
    public boolean isRegistered() {
        return true;
    }

    @Override
    public boolean login(String password) {
        return password != null && password.equals(this.password);
    }
}
