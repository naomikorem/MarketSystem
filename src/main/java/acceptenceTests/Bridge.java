package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

public abstract class Bridge {
    //TODO: change return tipes.
    //Acceptance Tests for use case 1:
    public abstract void enter();
    public abstract void exit();
    public abstract Response<User> register(String email, String name, String password);
    public abstract Response<User> login(String email, String password);
    //Acceptance Tests for use case 2:
    public abstract Response<Collection<Store>> getStores();
    public abstract Response<Store> getStoreInformation(String storeID);
    public abstract Response<Collection<Item>> searchProducts(String productName, String Category, List<String> keywords);
    public abstract Response<Collection<Item>> filterResults();


    //Acceptance Tests for use case 3:
    //Acceptance Tests for use case 4:
    //Acceptance Tests for use case 5:
    //Acceptance Tests for use case 6:
}
