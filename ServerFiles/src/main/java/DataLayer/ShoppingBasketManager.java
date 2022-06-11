package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ShoppingBasketDAL;
import DataLayer.DALObjects.UserDAL;

import java.util.HashMap;
import java.util.HashSet;

public class ShoppingBasketManager extends DALManager<ShoppingBasketDAL, Integer> {

    public ShoppingBasketManager() {
        super(ShoppingBasketDAL.class);
    }

    public static void main(String[] args) {
        /*
        UserDAL u = new UserDAL();
        u.setUserName("myName");
        u.setEmail("asdf");
        u.setPassword("asd");
        u.setLastName("asd");
        u.setFirstName("asaaa");
        (new UserManager()).addObject(u);

        u.setShoppingBaskets(new HashSet<>());

        ItemDAL i = new ItemDAL();
        i.setPrice(100);
        i.setProduct_name("blabla");
        (new ItemManager()).addObject(i);

        ShoppingBasketDAL s = new ShoppingBasketDAL();
        //s.setStoreId(5);
        //(new ShoppingBasketManager()).addObject(s);
        s.setItems(new HashMap<>());
        s.getItems().put(i, 5);

        (new ShoppingBasketManager()).addObject(s);

        u.getShoppingBaskets().add(s);
        (new UserManager()).addObject(u);*/
        UserDAL u = (new UserManager().getObject("myName"));
        System.out.println(u);


    }
}
