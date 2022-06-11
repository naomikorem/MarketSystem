package DataLayer;

import DataLayer.DALObjects.PredicateDAL;
import DataLayer.DALObjects.PurchasePolicyDAL;

public class PurchasePolicyManager extends DALManager<PurchasePolicyDAL, Integer> {

    private static class PurchasePolicyManagerHolder {
        static final PurchasePolicyManager instance = new PurchasePolicyManager();
    }

    public static PurchasePolicyManager getInstance() {
        return PurchasePolicyManager.PurchasePolicyManagerHolder.instance;
    }

    public PurchasePolicyManager() {
        super(PurchasePolicyDAL.class);
    }
}
