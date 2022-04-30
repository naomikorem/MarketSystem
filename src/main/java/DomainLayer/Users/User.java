package DomainLayer.Users;

import DomainLayer.Stores.Item;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public boolean isSubscribed() {
        return state.isSubscribed();
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

    public void addManagedStore(int storeId) {
        this.state.addManagedStore(storeId);
    }

    public void addOwnedStore(int storeId) {
        this.state.addOwnedStore(storeId);
    }

    public void removedManagedStore(int storeId) {
        this.state.removeManagedStore(storeId);
    }

    public void removedOwnedStore(int storeId) {
        this.state.removeOwnedStore(storeId);
    }

    public Set<Integer> getManagedStores() {
        return this.state.getManagedStores();
    }

    public Set<Integer> getOwnedStores() {
        return this.state.getOwnedStores();
    }

    public void emptyShoppingCart() {
        this.shoppingCart.emptyShoppingCart();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User) &&
                ((((User) other).isSubscribed() && ((User) other).getName().equals(getName())) || this == other);
    }

    @Override
    public int hashCode() {
        if (getName() != null) {
            return Objects.hashCode(this.getName());
        }
        return Objects.hashCode(this);
    }
}
