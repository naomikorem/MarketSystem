package DomainLayer.Stores.Predicates;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public abstract class AbstarctPredicate {
    public abstract boolean canApply(Item item, ShoppingBasket shoppingBasket);
    public boolean canApply(ShoppingBasket shoppingBasket) {
        return this.canApply(null, shoppingBasket);
    }
    public abstract String display();
}
