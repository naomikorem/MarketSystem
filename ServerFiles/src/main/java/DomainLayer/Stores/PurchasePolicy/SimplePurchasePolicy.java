package DomainLayer.Stores.PurchasePolicy;

import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Predicates.*;
import DomainLayer.Users.ShoppingBasket;
//import io.swagger.models.auth.In;

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
        return abstarctPredicate.canApply(sb) ;
    }

    @Override
    public synchronized void addAndPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        }
        AndCompositePredicate acp = new AndCompositePredicate();
        acp.addPredicate(predicate);
        acp.addPredicate(this.abstarctPredicate);
        this.abstarctPredicate = acp;
    }

    @Override
    public synchronized void addOrPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        }
        OrCompositePredicate acp = new OrCompositePredicate();
        acp.addPredicate(predicate);
        acp.addPredicate(this.abstarctPredicate);
        this.abstarctPredicate = acp;
    }

    @Override
    public synchronized void addCondPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        }
        CondCompositePredicate ccp = new CondCompositePredicate();
        ccp.addPredicate(predicate);
        ccp.addPredicate(this.abstarctPredicate);
        this.abstarctPredicate = ccp;
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
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getHour() {
        return this.hour;
    }

    public Calendar getDate() {
        return this.date;
    }
}
