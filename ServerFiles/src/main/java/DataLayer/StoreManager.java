package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.SimplePredicate;

import java.util.HashMap;
import java.util.HashSet;

public class StoreManager extends DALManager<StoreDAL, Integer> {

    private static class StoreManagerHolder {
        static final StoreManager instance = new StoreManager();
    }

    public static StoreManager getInstance() {
        return StoreManager.StoreManagerHolder.instance;
    }

    public StoreManager() {
        super(StoreDAL.class);
    }
}
