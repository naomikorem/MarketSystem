package DataLayer.DALObjects;

import DataLayer.DALObject;
import DomainLayer.Users.ShoppingCart;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserDAL implements DALObject<String> {
    @Id
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="storeOwners", joinColumns=@JoinColumn(name="owner"))
    @Column(name="store")
    private Set<Integer> ownedStores;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="storeManagers", joinColumns=@JoinColumn(name="manager"))
    @Column(name="store")
    private Set<Integer> managedStores;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="username")
    private Set<ShoppingBasketDAL> shoppingBaskets;


    public String getId() {
        return getUserName();
    }

    public void setId(String s) {
        this.setUserName(s);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Integer> getOwnedStores() {
        return ownedStores;
    }

    public void setOwnedStores(Set<Integer> ownedStores) {
        this.ownedStores = ownedStores;
    }

    public Set<Integer> getManagedStores() {
        return managedStores;
    }

    public void setManagedStores(Set<Integer> managedStores) {
        this.managedStores = managedStores;
    }

    public Set<ShoppingBasketDAL> getShoppingBaskets() {
        return shoppingBaskets;
    }

    public void setShoppingBaskets(Set<ShoppingBasketDAL> shoppingBaskets) {
        this.shoppingBaskets = shoppingBaskets;
    }

    public User toDomain() {
        SubscribedState state = new SubscribedState(getEmail(), getUserName(), getFirstName(), getLastName(), getPassword());
        state.setOwnedStores(getOwnedStores());
        state.setManagedStores(getManagedStores());

        User u = new User(state);
        ShoppingCart sc = new ShoppingCart();
        getShoppingBaskets().forEach(sb -> sc.addBasket(sb.toDomain()));
        u.setShoppingCart(sc);
        return u;
    }
}
