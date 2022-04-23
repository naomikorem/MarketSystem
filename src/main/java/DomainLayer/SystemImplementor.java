package DomainLayer;

import DomainLayer.Users.GuestState;
import DomainLayer.Users.User;

public class SystemImplementor {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private User user;

    //Add catch to every function here.

    public SystemImplementor() {
        this.user = new User(new GuestState());
        this.userFacade = new UserFacade();
    }

    public Response<User> register(String name, String password, String email) {
        return userFacade.register(name, password, email);
    }

    public Response<User> login(String name, String password) {
        Response<User> r = userFacade.login(name, password);
        if (!r.hadError()) {
            this.user = r.getObject();
        }
        return r;
    }


    public void addManager(User owner, String manager, int storeId) {
        if (userFacade.isExist(manager)) {
            throw new IllegalArgumentException(String.format("There is no user by the name of %s", manager));
        }
        storeFacade.addManager(owner, manager, storeId);
    }
}
