package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.List;

public abstract class ComositePredicate extends AbstarctPredicate {
    private List<AbstarctPredicate> preds;

    public ComositePredicate(List<AbstarctPredicate> preds) {
        this.preds = preds;
    }

    protected abstract boolean applyOperation(boolean pred1, boolean pred2);

    @Override
    boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        if (preds == null || preds.isEmpty())
            return true;
        boolean output = preds.get(0).canApply(item, shoppingBasket);
        for (int i = 1; i < preds.size(); i++) {
            output = applyOperation(output, preds.get(i).canApply(item, shoppingBasket));
        }
        return output;
    }
}
