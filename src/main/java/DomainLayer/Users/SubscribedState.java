package DomainLayer.Users;

import Exceptions.LogException;
import Utility.Utility;
import Utility.LogUtility;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SubscribedState implements UserState {
    private static final int MAX_NAME_LENGTH = 25;
    private static final int MIN_NAME_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 25;
    private static final int MIN_PASSWORD_LENGTH = 4;

    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Set<Integer> ownedStores;
    private Set<Integer> managedStores;

    public SubscribedState(String email, String userName, String firstName, String lastName, String password) {
        checkParameters(email,userName,firstName,lastName, password);
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(UserController.SALT_HASH_ROUND_COUNT));
        this.email = email;
        this.ownedStores = new HashSet<>();
        this.managedStores = new HashSet<>();
    }

    public static boolean isValidUsername(String name) {
        return name != null && name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH && Pattern.matches("^[a-zA-Z0-9]+$", name);
    }

    private void checkParameters(String email, String userName, String firstName, String lastName, String password) {
        if (!isValidUsername(userName) ) {
            throw new IllegalArgumentException("A user name/ last name must be at least 4 letters long.");
        }
        if (!isValidUsername(firstName) ) {
            throw new IllegalArgumentException("A user name/ last name must be at least 4 letters long.");
        }

        if (!isValidUsername(lastName) ) {
            throw new IllegalArgumentException("A user name/ last name must be at least 4 letters long.");
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
        return this.userName;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    public void setName(String userName){
        if (!isValidUsername(userName)) {
            throw new IllegalArgumentException("A user name must be at least 4 letters long.");
        }
        String oldName = this.userName;
        this.userName = userName;
        LogUtility.info(String.format("User %s changed its name to %s", oldName, this.userName));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail){
        if (newEmail == null || !Utility.isValidEmailAddress(newEmail)) {
            throw new IllegalArgumentException("The email is invalid.");
        }
        this.email = newEmail;
        LogUtility.info(String.format("User %s changed its email to %s", this.userName, this.email));
    }

    @Override
    public void addManagedStore(int storeId) {
        this.managedStores.add(storeId);
    }

    @Override
    public void addOwnedStore(int storeId) {
        this.ownedStores.add(storeId);
    }

    @Override
    public void removeManagedStore(int storeId) {
        this.managedStores.remove(storeId);
    }

    @Override
    public void removeOwnedStore(int storeId) {
        this.ownedStores.remove(storeId);
    }

    @Override
    public Set<Integer> getOwnedStores() {
        return ownedStores;
    }

    @Override
    public Set<Integer> getManagedStores() {
        return managedStores;
    }

    @Override
    public boolean isSubscribed() {
        return true;
    }

    @Override
    public boolean login(String password) {
        return password != null && BCrypt.checkpw(password, this.password);
    }
}
