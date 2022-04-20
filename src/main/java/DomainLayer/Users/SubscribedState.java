package DomainLayer.Users;

public class SubscribedState implements UserState {
    private int id;
    private String name;
    private String password;
    private String email;

    public SubscribedState(int id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Override
    public int getId() {
        return id;
    }
}
