package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Predicates.AbstarctPredicate;
import DomainLayer.Stores.Predicates.AndCompositePredicate;
import DomainLayer.Stores.Predicates.OrCompositePredicate;
import DomainLayer.Stores.Predicates.XorCompositePredicate;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Map;

public class SimpleDiscountPolicy extends AbstractDiscountPolicy {

    private double percentage;
    private AbstarctPredicate abstarctPredicate;

    public SimpleDiscountPolicy(double percentage, AbstarctPredicate abstarctPredicate) {
        super();
        this.percentage = percentage;
        this.abstarctPredicate = abstarctPredicate;
    }

    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        sb.getItems().stream().filter(i -> abstarctPredicate == null || abstarctPredicate.canApply(i, sb)).forEach(i -> discounts.put(i, Math.min(discounts.getOrDefault(i, 0.0) + percentage, 1)));
        return sb.calculatePrice(discounts);
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
    public synchronized void addXorPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        }
        XorCompositePredicate acp = new XorCompositePredicate();
        acp.addPredicate(predicate);
        acp.addPredicate(this.abstarctPredicate);
        this.abstarctPredicate = acp;
    }
}
