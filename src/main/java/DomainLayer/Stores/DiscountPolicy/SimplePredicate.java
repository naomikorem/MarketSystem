package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

public class SimplePredicate extends AbstarctPredicate {
    private Integer id;
    private Category category;

    public SimplePredicate(int id) {
        this.id = id;
        this.category = null;
    }

    public SimplePredicate(Category category) {
        this.id = null;
        this.category = category;
    }

    @Override
    boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        if (this.id == null)
            return item.getCategory().equals(category);
        if (this.category == null)
            return item.getId() == id;
        return item.getCategory().equals(category) && item.getId() == id;
    }
}
