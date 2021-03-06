package DomainLayer.Stores.PurchasePolicy;

import DataLayer.DALObjects.PurchasePolicyDAL;
import DataLayer.DALObjects.SimplePurchasePolicyDAL;
import DataLayer.PredicateManager;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Predicates.*;
import DomainLayer.Stores.StoreController;
import DomainLayer.Users.ShoppingBasket;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SimplePurchasePolicy extends AbstractPurchasePolicy{
    private AbstarctPredicate abstarctPredicate;
    private int hour;
    private Calendar date;
    public SimplePurchasePolicy(AbstarctPredicate abstarctPredicate) {
        super();
        checkFields(hour);
        this.hour= 24;
        this.date = null;
        this.abstarctPredicate = abstarctPredicate;
    }

    public List<SimplePurchasePolicy> getAllPurchasePolicies() {
        return List.of(this);
    }

    @Override
    public boolean applyPolicy(ShoppingBasket sb) {
        if(abstarctPredicate == null){
            return true;
        }
        if(!abstarctPredicate.canApply(sb)) {
            throw new IllegalArgumentException("The purchase could not apply the purchase policies for store "+ StoreController.getInstance().getStore(sb.getStoreId()).getName()+"\nbecause the following the predicate doesn't apply: " + abstarctPredicate.display());
        }
        return true;
    }

    @Override
    public synchronized void addAndPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
            return;
        }
        AndCompositePredicate acp = new AndCompositePredicate();
        acp.addPredicate(predicate);
        acp.addPredicate(this.abstarctPredicate);
        acp.setId(PredicateManager.getInstance().addObject(acp.toDAL()));
        this.abstarctPredicate = acp;
        update();
    }

    @Override
    public synchronized void addOrPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
            return;
        }
        OrCompositePredicate acp = new OrCompositePredicate();
        acp.addPredicate(predicate);
        acp.addPredicate(this.abstarctPredicate);
        acp.setId(PredicateManager.getInstance().addObject(acp.toDAL()));
        this.abstarctPredicate = acp;
        update();
    }

    @Override
    public synchronized void addCondPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
            return;
        }
        CondCompositePredicate ccp = new CondCompositePredicate();
        ccp.addPredicate(predicate);
        ccp.addPredicate(this.abstarctPredicate);
        ccp.setId(PredicateManager.getInstance().addObject(ccp.toDAL()));
        this.abstarctPredicate = ccp;
        update();
    }

    public String display() {
        return this.abstarctPredicate == null ? "true" : this.abstarctPredicate.display();
    }

    private void checkFields(int hour) {
        if (hour < 0 || hour > 24) {
            throw new IllegalArgumentException("Hour cannot be lower than 0 or higher than 24");
        }
    }
    public void setHour(int hour) {
        checkFields(hour);
        this.hour = hour;
        update();
    }

    public void setDate(Calendar date) {
        this.date = date;
        update();
    }

    public int getHour() {
        return this.hour;
    }

    public Calendar getDate() {
        return this.date;
    }

    @Override
    public PurchasePolicyDAL toDAL() {
        SimplePurchasePolicyDAL res = new SimplePurchasePolicyDAL();
        res.setId(getId());
        res.setHour(getHour());
        res.setDate(getDate() == null ? null : getDate().getTime());
        res.setPredicate(abstarctPredicate == null ? null : abstarctPredicate.toDAL());
        return res;
    }
}
