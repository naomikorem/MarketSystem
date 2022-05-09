package DomainLayer.Stores.DiscountPolicy.Predicates;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public class SimplePredicate extends AbstarctPredicate {

    public interface ItemPredicateCallback {
        public boolean checkItem(Item i);
    }

    public interface BasketPredicateCallback {
        public boolean checkBasket(ShoppingBasket i);
    }

    private ItemPredicateCallback ipc;
    private BasketPredicateCallback bpc;

    public SimplePredicate(int itemId) {
        this.ipc = (i) -> i.getId() == itemId;
        this.bpc = (b) -> true;
    }

    public SimplePredicate(Category category) {
        this.ipc = (i) -> i.getCategory().equals(category);
        this.bpc = (b) -> true;
    }

    public SimplePredicate(BasketPredicateCallback bpc) {
        this.ipc = (i) -> true;
        this.bpc = bpc;
    }


    @Override
    public boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        return ipc.checkItem(item) && bpc.checkBasket(shoppingBasket);
    }
}
