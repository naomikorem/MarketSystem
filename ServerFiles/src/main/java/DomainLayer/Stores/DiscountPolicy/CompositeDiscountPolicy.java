package DomainLayer.Stores.DiscountPolicy;

import DataLayer.DiscountManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CompositeDiscountPolicy extends AbstractDiscountPolicy {
    protected List<AbstractDiscountPolicy> discountPolicies;


    public CompositeDiscountPolicy() {
        super();
        this.discountPolicies = new ArrayList<>();
    }

    public void addDiscount(AbstractDiscountPolicy adc) {
        this.discountPolicies.add(adc);
        update();
    }

    public void setDiscountPolicies(List<AbstractDiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public List<AbstractDiscountPolicy> getDiscountPolicies() {
        return this.discountPolicies;
    }

    @Override
    public List<SimpleDiscountPolicy> getAllDiscountPolicies() {
        return getDiscountPolicies().stream().map(AbstractDiscountPolicy::getAllDiscountPolicies).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
