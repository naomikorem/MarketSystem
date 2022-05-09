package DomainLayer.Stores.DiscountPolicy;

import java.util.List;

public class AndCompositePredicate extends ComositePredicate {
    public AndCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 && pred2);
    }
}
