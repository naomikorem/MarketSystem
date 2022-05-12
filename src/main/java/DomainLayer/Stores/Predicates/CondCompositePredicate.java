package DomainLayer.Stores.Predicates;


import java.util.List;

public class CondCompositePredicate extends CompositePredicate {
    public CondCompositePredicate() {
        super();
    }

    public CondCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return ((pred1 && pred2) || (!pred1));
    }
}
