package DomainLayer.Stores.PurchasePolicy;

import DataLayer.DALObjects.PurchasePolicyDAL;
import DataLayer.PurchasePolicyManager;
import DomainLayer.Stores.DiscountPolicy.CompositeDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Predicates.AbstarctPredicate;
import DomainLayer.Users.ShoppingBasket;

import java.util.*;

public abstract class AbstractPurchasePolicy {
    private int id;

    public AbstractPurchasePolicy() {}

    public abstract boolean applyPolicy(ShoppingBasket sb);


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<AbstractPurchasePolicy> getPurchasesPolicies() {
        return null;
    }

    public abstract List<SimplePurchasePolicy> getAllPurchasePolicies();

    public AbstractPurchasePolicy getPolicies(int policyId) {
        if (getId() == policyId) {
            return this;
        }
        if (this.getPurchasesPolicies() == null) {
            return null;
        }
        AbstractPurchasePolicy policy = getPurchasesPolicies().stream().filter(d -> d.getId() == policyId).findFirst().orElse(null);
        if (policy != null) {
            return policy;
        }
        for (AbstractPurchasePolicy app : getPurchasesPolicies()) {
            AbstractPurchasePolicy res = app.getPolicies(policyId);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public boolean removePolicy(int policyId) {
        if (getPurchasesPolicies() == null) {
            return false;
        }
        AbstractPurchasePolicy policy = getPurchasesPolicies().stream().filter(d -> d.getId() == policyId).findFirst().orElse(null);
        if (policy != null) {
            getPurchasesPolicies().remove(policy);
            return true;
        }
        for (AbstractPurchasePolicy app : getPurchasesPolicies()) {
            boolean removed = app.removePolicy(policyId);
            if (removed) {
                return true;
            }
        }
        return false;
    }

    public synchronized void addAndPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This purchase policy does not have a predicate");
    }

    public synchronized void addOrPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This purchase policy does not have a predicate");
    }

    //conditioning
    public synchronized void addCondPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This purchase policy does not have a predicate");
    }

    public void setHour(int hour) {
        throw new IllegalArgumentException("This discount policy does not have a percentage field.");
    }

    public void setDate(Calendar date) {
        throw new IllegalArgumentException("This discount policy does not have a percentage field.");
    }

    public abstract PurchasePolicyDAL toDAL();

    public void update() {
        if (getId() != 0) {
            PurchasePolicyManager.getInstance().addObject(toDAL());
        }
    }
}
