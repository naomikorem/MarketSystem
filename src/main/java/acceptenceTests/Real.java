package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import DomainLayer.Users.User;

import java.util.List;

public class Real extends Bridge {
    private SystemInterface adaptee;

    public Real() {
        this.adaptee = new SystemImplementor();
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public Response<User> register(String email, String name, String password) {
        return adaptee.register(email, name, password);
    }

    @Override
    public Response<User> login(String name, String password) {
        return adaptee.login(name, password);
    }

    @Override
    public void getStores() {

    }

    @Override
    public void getStoreInformation(String storeID) {

    }

    @Override
    public void searchProducts(String productName, String Category, List<String> keywords) {

    }

    @Override
    public void filterResults() {

    }
}
