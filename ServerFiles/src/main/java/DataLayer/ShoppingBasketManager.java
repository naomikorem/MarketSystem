package DataLayer;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.DALObjects.ShoppingBasketDAL;
import DataLayer.DALObjects.UserDAL;

import java.util.HashMap;
import java.util.HashSet;

public class ShoppingBasketManager extends DALManager<ShoppingBasketDAL, Integer> {

    private static class ShoppingBasketManagerHolder {
        static final ShoppingBasketManager instance = new ShoppingBasketManager();
    }

    public static ShoppingBasketManager getInstance() {
        return ShoppingBasketManager.ShoppingBasketManagerHolder.instance;
    }

    public ShoppingBasketManager() {
        super(ShoppingBasketDAL.class);
    }
}
