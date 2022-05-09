package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;
import Exceptions.LogException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDiscountPolicy {

    protected double percentage;

    public AbstractDiscountPolicy(double percentage) {
        if (percentage < 0) {
            throw new LogException("You cannot create a discount policy with negative percentage", "There was an attempt to add a discount with negative percentage.");
        }
        this.percentage = percentage;
    }

    public double applyDiscount(ShoppingBasket sb) {
        return this.applyDiscount(sb, new HashMap<>());
    }

    public abstract double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts);
}
