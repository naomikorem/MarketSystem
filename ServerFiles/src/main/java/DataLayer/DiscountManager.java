package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Predicates.SimplePredicate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DiscountManager extends DALManager<DiscountDAL, Integer> {

    private static class DiscountManagerHolder {
        static final DiscountManager instance = new DiscountManager();
    }

    public static DiscountManager getInstance() {
        return DiscountManager.DiscountManagerHolder.instance;
    }
    
    public DiscountManager() {
        super(DiscountDAL.class);
    }
    
}
