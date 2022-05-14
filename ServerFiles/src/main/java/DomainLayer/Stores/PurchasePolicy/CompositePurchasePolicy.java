package DomainLayer.Stores.PurchasePolicy;

import java.util.ArrayList;
import java.util.List;

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

}
