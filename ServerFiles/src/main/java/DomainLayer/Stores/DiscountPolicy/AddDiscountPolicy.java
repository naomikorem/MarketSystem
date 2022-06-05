package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.Map;

public class AddDiscountPolicy extends CompositeDiscountPolicy {
    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        double price = 0;
        for (AbstractDiscountPolicy dp : discountPolicies) {
            price = dp.applyDiscount(sb, discounts);
        }
        return price;
    }

    @Override
    public Map<Item, Double> getDiscounts(ShoppingBasket sb, Map<Item, Double> discounts) {
        for (AbstractDiscountPolicy dp : discountPolicies) {
            dp.getDiscounts(sb, discounts);
        }
        return discounts;
    }

}
