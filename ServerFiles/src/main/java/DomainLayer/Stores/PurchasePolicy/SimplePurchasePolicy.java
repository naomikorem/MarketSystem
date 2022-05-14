package DomainLayer.Stores.PurchasePolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Predicates.*;
import DomainLayer.Users.ShoppingBasket;

import java.util.Map;

public class SimplePurchasePolicy extends AbstractPurchasePolicy{
    private AbstarctPredicate abstarctPredicate;

    public SimplePurchasePolicy(AbstarctPredicate abstarctPredicate) {
        super();
        this.abstarctPredicate = abstarctPredicate;
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
}
