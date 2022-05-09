package DomainLayer.Stores.DiscountPolicy;

        import java.util.List;

public class XorCompositePredicate extends ComositePredicate {
    public XorCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 ^ pred2);
    }
}
