package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.ArrayList;
import java.util.List;

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

    public boolean isLoggedIn() {
        return state.isLoggedIn();
    }

    public boolean login(String password) {
        return state.login(password);
    }

    public List<Item> getShoppingCartItems () {
        return shoppingCart.getAllItems();
    }
    public List<ShoppingBasket> getCartBaskets() {
        return shoppingCart.getBaskets();
    }

    public void addItemToShoppingCart(int storeId, Item item) {
        this.shoppingCart.addItem(storeId, item);
    }
}
