package DomainLayer.Stores.PurchasePolicy;

import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CompositePurchasePolicy extends AbstractPurchasePolicy {
    protected List<AbstractPurchasePolicy> purchasePolicies;


    public CompositePurchasePolicy() {
        super();
        this.purchasePolicies = new ArrayList<>();
    }

    public void addPolicy(AbstractPurchasePolicy app) {
        this.purchasePolicies.add(app);
    }

    @Override
    public List<AbstractPurchasePolicy> getPurchasesPolicies() {
        return this.purchasePolicies;
    }

    public List<SimplePurchasePolicy> getAllPurchasePolicies() {
        return getPurchasesPolicies().stream().map(AbstractPurchasePolicy::getAllPurchasePolicies).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void setPurchasePolicies(List<AbstractPurchasePolicy> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }
}
