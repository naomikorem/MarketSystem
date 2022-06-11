package DomainLayer.Stores.Predicates;

import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public abstract class AbstarctPredicate {
    private int id;
    public abstract boolean canApply(Item item, ShoppingBasket shoppingBasket);
    public boolean canApply(ShoppingBasket shoppingBasket) {
        return this.canApply(null, shoppingBasket);
    }
    public abstract String display();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
