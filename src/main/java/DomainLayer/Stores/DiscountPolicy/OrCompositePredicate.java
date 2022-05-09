package DomainLayer.Stores.DiscountPolicy;

import java.util.List;

public class OrCompositePredicate extends ComositePredicate {
    public OrCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 || pred2);
    }
}
