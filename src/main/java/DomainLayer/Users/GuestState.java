package DomainLayer.Users;

public class GuestState implements UserState {
    @Override
    public int getId() {
        return -1;
    }
}
