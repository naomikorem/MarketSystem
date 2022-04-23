package DomainLayer.Users;

public interface UserState {
    public String getName();
    public boolean isLoggedIn();
    public boolean login(String password);
}
