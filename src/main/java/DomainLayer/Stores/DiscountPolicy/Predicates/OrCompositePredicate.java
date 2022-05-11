package DomainLayer.Stores.DiscountPolicy.Predicates;

import java.util.List;

public class OrCompositePredicate extends CompositePredicate {
    public OrCompositePredicate() {
        super();
    }

    public OrCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 || pred2);
    }
}
