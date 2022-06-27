package DomainLayer.Stores.DiscountPolicy;

import DataLayer.DALObjects.CompositeDiscountDAL;
import DataLayer.DALObjects.DiscountDAL;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MaxDiscountPolicy extends CompositeDiscountPolicy {
    @Override
    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        return getMax(sb, discounts).applyDiscount(sb, discounts);
    }

    @Override
    public Map<Item, Double> getDiscounts(ShoppingBasket sb, Map<Item, Double> discounts) {
        return getMax(sb, discounts).getDiscounts(sb, discounts);
    }
    private  AbstractDiscountPolicy getMax(ShoppingBasket sb, Map<Item, Double> discounts){
        double price = this.discountPolicies.get(0).applyDiscount(sb, new HashMap<>(discounts));
        AbstractDiscountPolicy max = this.discountPolicies.get(0);
        for (int i = 1; i < this.discountPolicies.size(); i++) {
            double newPrice = discountPolicies.get(i).applyDiscount(sb, new HashMap<>(discounts));
            if (newPrice < price) {
                price = newPrice;
                max = discountPolicies.get(i);
            }
        }
        return max;
    }

    @Override
    public DiscountDAL toDAL() {
        CompositeDiscountDAL res = new CompositeDiscountDAL();
        res.setId(getId());
        res.setType(CompositeDiscountDAL.CompositeDiscountType.Max);
        res.setDiscountPolicies(getDiscountPolicies().stream().map(AbstractDiscountPolicy::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
