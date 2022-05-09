package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;
import Exceptions.LogException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDiscountPolicy {
    
    public AbstractDiscountPolicy() {}

    public double applyDiscount(ShoppingBasket sb) {
        return this.applyDiscount(sb, new HashMap<>());
    }

    public abstract double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts);
}
