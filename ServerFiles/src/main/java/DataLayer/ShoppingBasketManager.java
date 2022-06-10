package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ShoppingBasketDAL;

import java.util.HashMap;

public class ShoppingBasketManager extends DALManager<ShoppingBasketDAL, Integer> {

    public ShoppingBasketManager() {
        super(ShoppingBasketDAL.class);
    }

    public static void main(String[] args) {
        ItemDAL i = new ItemDAL();
        i.setPrice(100);
        i.setProduct_name("blabla");
        (new ItemManager()).addObject(i);

        ShoppingBasketDAL s = new ShoppingBasketDAL();
        s.setUsername("myName");
        //(new ShoppingBasketManager()).addObject(s);
        s.setItems(new HashMap<>());
        s.getItems().put(i, 5);

        (new ShoppingBasketManager()).addObject(s);

    }
}
