package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.CompositePredicate;
import DomainLayer.Stores.Predicates.SimplePredicate;
import Utility.LogUtility;

import java.util.ArrayList;
import java.util.Calendar;

public class PredicateManager extends DALManager<PredicateDAL, Integer> {

    private static class PredicateManagerHolder {
        static final PredicateManager INSTANCE = new PredicateManager();
    }

    private PredicateManager() {
        super(PredicateDAL.class);
    }

    // Implementation of thread safe singleton
    public static PredicateManager getInstance() {
        return PredicateManager.PredicateManagerHolder.INSTANCE;
    }
}
