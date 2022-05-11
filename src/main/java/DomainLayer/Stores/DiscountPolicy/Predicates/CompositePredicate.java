package DomainLayer.Stores.DiscountPolicy.Predicates;

import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositePredicate extends AbstarctPredicate {

    public enum PredicateEnum {
        AND,
        OR,
        XOR,
    }

    private List<AbstarctPredicate> preds;

    public CompositePredicate() {
        this.preds = new ArrayList<>();
    }

    public void addPredicate(AbstarctPredicate ap) {
        this.preds.add(ap);
    }

    public CompositePredicate(List<AbstarctPredicate> preds) {
        this.preds = preds;
    }

    protected abstract boolean applyOperation(boolean pred1, boolean pred2);

    @Override
    public boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        if (preds == null || preds.isEmpty())
            return true;
        boolean output = preds.get(0).canApply(item, shoppingBasket);
        for (int i = 1; i < preds.size(); i++) {
            output = applyOperation(output, preds.get(i).canApply(item, shoppingBasket));
        }
        return output;
    }

}
