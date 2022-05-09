package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;
import Exceptions.LogException;

import java.util.Map;

public class BasicDiscountPolicy extends AbstractDiscountPolicy {

    protected double percentage;

    public BasicDiscountPolicy(double percentage) {
        super(percentage);
    }


    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        sb.getItems().forEach(i -> discounts.put(i, discounts.getOrDefault(i, 0.0) + percentage));
        return sb.calculatePrice(discounts);
    }
}
