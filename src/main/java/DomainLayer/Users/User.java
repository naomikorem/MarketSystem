package DomainLayer.Users;

public class User {
    private UserState state;
    private ShoppingCart shoppingCart;

    public User(UserState state) {
        this.state = state;
        this.shoppingCart = new ShoppingCart();
    }

    public String getName() {
        return state.getName();
    }
}
