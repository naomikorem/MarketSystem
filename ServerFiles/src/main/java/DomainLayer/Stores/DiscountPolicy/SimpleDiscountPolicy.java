package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Predicates.AbstarctPredicate;
import DomainLayer.Stores.Predicates.AndCompositePredicate;
import DomainLayer.Stores.Predicates.OrCompositePredicate;
import DomainLayer.Stores.Predicates.XorCompositePredicate;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.List;
import java.util.Map;

public class SimpleDiscountPolicy extends AbstractDiscountPolicy {

    private double percentage;
    private AbstarctPredicate abstarctPredicate;

    public SimpleDiscountPolicy(double percentage, AbstarctPredicate abstarctPredicate) {
        super();
        checkFields(percentage);
        this.percentage = percentage;
        this.abstarctPredicate = abstarctPredicate;
    }

    private void checkFields(double percentage) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Discount cannot be lower than 0 or higher than 1");
        }
    }

    public double getPercentage() {
        return this.percentage;
    }

    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        sb.getItems().stream().filter(i -> abstarctPredicate == null || abstarctPredicate.canApply(i, sb)).forEach(i -> discounts.put(i, Math.min(discounts.getOrDefault(i, 0.0) + percentage, 1)));
        return sb.calculatePrice(discounts);
    }

    @Override
    public Map<Item, Double> getDiscounts(ShoppingBasket sb, Map<Item, Double> discounts) {
        sb.getItems().stream().filter(i -> abstarctPredicate == null || abstarctPredicate.canApply(i, sb)).forEach(i -> discounts.put(i, Math.min(discounts.getOrDefault(i, 0.0) + percentage, 1)));
        return discounts;
    }

    @Override
    public List<SimpleDiscountPolicy> getAllDiscountPolicies() {
        return List.of(this);
    }

    @Override
    public synchronized void addAndPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        } else {
            AndCompositePredicate acp = new AndCompositePredicate();
            acp.addPredicate(predicate);
            acp.addPredicate(this.abstarctPredicate);
            this.abstarctPredicate = acp;
        }
    }

    @Override
    public synchronized void addOrPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        } else {
            OrCompositePredicate acp = new OrCompositePredicate();
            acp.addPredicate(predicate);
            acp.addPredicate(this.abstarctPredicate);
            this.abstarctPredicate = acp;
        }
    }

    @Override
    public synchronized void addXorPredicate(AbstarctPredicate predicate) {
        if(this.abstarctPredicate == null) {
            this.abstarctPredicate = predicate;
        } else {
            XorCompositePredicate acp = new XorCompositePredicate();
            acp.addPredicate(predicate);
            acp.addPredicate(this.abstarctPredicate);
            this.abstarctPredicate = acp;
        }
    }

    public String display() {
        return this.abstarctPredicate == null ? "true" : this.abstarctPredicate.display();
    }

    public void setPercentage(double percentage) {
        checkFields(percentage);
        this.percentage = percentage;
    }

    public void setAbstarctPredicate(AbstarctPredicate abstarctPredicate) {
        this.abstarctPredicate = abstarctPredicate;
    }
}
