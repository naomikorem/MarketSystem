package DomainLayer.Stores.DiscountPolicy.Predicates;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public abstract class AbstarctPredicate {
    public abstract boolean canApply(Item item, ShoppingBasket shoppingBasket);
}
