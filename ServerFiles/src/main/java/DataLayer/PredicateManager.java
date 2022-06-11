package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.CompositePredicate;
import DomainLayer.Stores.Predicates.SimplePredicate;

import java.util.ArrayList;
import java.util.Calendar;

public class PredicateManager extends DALManager<PredicateDAL, Integer> {
    public PredicateManager() {
        super(PredicateDAL.class);
    }

    public static void main(String[] args) {
        PredicateDAL p = (new PredicateManager()).getObject(6);
        System.out.println(p);
    }
}
