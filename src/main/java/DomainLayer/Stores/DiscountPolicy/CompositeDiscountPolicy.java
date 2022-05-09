package DomainLayer.Stores.DiscountPolicy;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeDiscountPolicy extends AbstractDiscountPolicy {
    protected List<AbstractDiscountPolicy> discountPolicies;


    public CompositeDiscountPolicy() {
        this.discountPolicies = new ArrayList<>();
    }

    public void addDiscount(AbstractDiscountPolicy adc) {
        this.discountPolicies.add(adc);
    }
}
