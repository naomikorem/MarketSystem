package DomainLayer.Stores.DiscountPolicy;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeDiscountPolicy extends AbstractDiscountPolicy {
    protected List<AbstractDiscountPolicy> discountPolicies;


    public CompositeDiscountPolicy(double percentage) {
        super(percentage);
        this.discountPolicies = new ArrayList<>();
    }
}
