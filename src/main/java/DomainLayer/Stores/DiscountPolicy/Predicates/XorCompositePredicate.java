package DomainLayer.Stores.DiscountPolicy.Predicates;

        import java.util.List;

public class XorCompositePredicate extends CompositePredicate {

    public XorCompositePredicate() {
        super();
    }

    public XorCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 ^ pred2);
    }
}
