package DataLayer;

import DataLayer.DALObjects.PredicateDAL;
import DataLayer.DALObjects.PurchasePolicyDAL;

public class PurchasePolicyManager extends DALManager<PurchasePolicyDAL, Integer> {
    public PurchasePolicyManager() {
        super(PurchasePolicyDAL.class);
    }
}
