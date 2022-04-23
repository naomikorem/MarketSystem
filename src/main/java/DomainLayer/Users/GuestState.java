package DomainLayer.Users;

public class GuestState implements UserState {
    @Override
    public String getName() {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public boolean login(String password) {
        return false;
    }
}
