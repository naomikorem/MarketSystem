package DomainLayer.Users;

public class SubscribedState implements UserState {
    private String name;
    private String password;
    private String email;

    public SubscribedState(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
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
