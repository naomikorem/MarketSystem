package DomainLayer.Stores.DiscountPolicy;

import DataLayer.DALObjects.CompositeDiscountDAL;
import DataLayer.DALObjects.DiscountDAL;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public DiscountDAL toDAL() {
        CompositeDiscountDAL res = new CompositeDiscountDAL();
        res.setId(getId());
        res.setType(CompositeDiscountDAL.CompositeDiscountType.Add);
        res.setDiscountPolicies(getDiscountPolicies().stream().map(AbstractDiscountPolicy::toDAL).collect(Collectors.toSet()));
        return res;
    }

}
