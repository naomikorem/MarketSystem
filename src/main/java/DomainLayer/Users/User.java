package DomainLayer.Users;

import DomainLayer.Stores.Item;

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

    public String getEmail() {
        return state.getEmail();
    }

    public boolean isRegistered() {
        return state.isRegistered();
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

    public void addItemToShoppingCart(int storeId, Item item, int amount) {
        this.shoppingCart.addItem(storeId, item, amount);
    }

    public UserState getState() {
        return this.state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public void setName(String name){
        this.state.setName(name);
    }

    public void setEmail(String email){
        this.state.setEmail(email);
    }
}
