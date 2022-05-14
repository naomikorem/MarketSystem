package DomainLayer.Stores.Predicates;

import java.util.List;

public class AndCompositePredicate extends CompositePredicate {

    public AndCompositePredicate() {
        super();
    }
    public AndCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 && pred2);
    }
}
