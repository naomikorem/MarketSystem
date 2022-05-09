package DomainLayer.Stores.DiscountPolicy;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Map;

public class ItemDiscountPolicy extends AbstractDiscountPolicy {
    public interface ItemDiscountPredicate {
        boolean canApply(Item item);
    }

    public interface ShoppingBasketDiscountPredicate {
        boolean canApply(ShoppingBasket shoppingBasket);
    }

    private ItemDiscountPredicate idp;
    private ShoppingBasketDiscountPredicate sbdp;

    public ItemDiscountPolicy(double percentage, ItemDiscountPredicate idp, ShoppingBasketDiscountPredicate sbdp) {
        super(percentage);
        this.idp = idp;
        this.sbdp = sbdp;
    }

    public ItemDiscountPolicy(double percentage, int itemId) {
        this(percentage, (Item item) -> item.getId() == itemId, (ShoppingBasket sb) -> true);
    }

    public ItemDiscountPolicy(double percentage, Category c) {
        this(percentage, (Item item) -> item.getCategory().equals(c), (ShoppingBasket sb) -> true);
    }

    public ItemDiscountPolicy(double percentage, double minTotalPrice) {
        this(percentage, (Item i) -> true, (ShoppingBasket sb) -> sb.calculatePrice() >= minTotalPrice);
    }


    public double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts) {
        if (sbdp.canApply(sb)) {
            sb.getItems().stream().filter(i -> idp.canApply(i)).forEach(i -> discounts.put(i, discounts.getOrDefault(i, 0.0) + percentage));
            return sb.calculatePrice(discounts);
        }
        return sb.calculatePrice();
    }

    public AbstarctPredicate minBasketTotalPrice(double minTotalPrice) {
        return new AbstarctPredicate() {
            @Override
            boolean canApply(Item item, ShoppingBasket shoppingBasket) {
                return shoppingBasket.calculatePrice() >= minTotalPrice;
            }
        };
    }


}
