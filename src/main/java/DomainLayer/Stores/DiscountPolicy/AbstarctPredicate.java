package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public abstract class AbstarctPredicate {
    abstract boolean canApply(Item item, ShoppingBasket shoppingBasket);
}
