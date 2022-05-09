package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.DiscountPolicy.Predicates.AbstarctPredicate;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Map;

public class SimpleDiscountPolicy extends AbstractDiscountPolicy {

    private double percentage;
    private AbstarctPredicate abstarctPredicate;

    public SimpleDiscountPolicy(double percentage, AbstarctPredicate abstarctPredicate) {
        this.percentage = percentage;
        this.abstarctPredicate = abstarctPredicate;
    }

    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        sb.getItems().stream().filter(i -> abstarctPredicate.canApply(i, sb)).forEach(i -> discounts.put(i, discounts.getOrDefault(i, 0.0) + percentage));
        return sb.calculatePrice(discounts);
    }
}
