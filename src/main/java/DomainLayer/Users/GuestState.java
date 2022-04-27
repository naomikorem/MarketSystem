package DomainLayer.Users;

public class GuestState implements UserState {
    @Override
    public String getName() {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public String getEmail() {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public boolean login(String password) {
        return false;
    }

    @Override
    public void setName(String name) {
        throw new RuntimeException("Guest user does not have a name field");
    }

    @Override
    public void setEmail(String email) {
        throw new RuntimeException("Guest user does not have a name field");
    }
}
