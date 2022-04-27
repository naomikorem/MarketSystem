package DomainLayer.Users;

public interface UserState {
    public String getName();
    public boolean isRegistered();
    public boolean login(String password);
    public void setName(String name);
    public void setEmail(String email);
}
