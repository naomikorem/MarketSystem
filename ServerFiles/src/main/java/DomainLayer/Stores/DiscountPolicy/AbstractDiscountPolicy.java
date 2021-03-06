package DomainLayer.Stores.DiscountPolicy;

import DataLayer.DALObjects.CompositePredicateDAL;
import DataLayer.DALObjects.DiscountDAL;
import DataLayer.DALObjects.PredicateDAL;
import DataLayer.DiscountManager;
import DomainLayer.Stores.Predicates.AbstarctPredicate;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Predicates.CompositePredicate;
import DomainLayer.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractDiscountPolicy {

    private int id;
    
    public AbstractDiscountPolicy() {}

    public double applyDiscount(ShoppingBasket sb) {
        return this.applyDiscount(sb, new HashMap<>());
    }
    public abstract double applyDiscount(ShoppingBasket sb, Map<Item, Double> discounts);

    public Map<Item, Double> getDiscounts(ShoppingBasket sb){
        return this.getDiscounts(sb,   new HashMap<>());
    }

    public abstract  Map<Item, Double> getDiscounts(ShoppingBasket sb, Map<Item, Double> discounts);

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<AbstractDiscountPolicy> getDiscountPolicies() {
        return null;
    }

    public abstract List<SimpleDiscountPolicy> getAllDiscountPolicies();

    public AbstractDiscountPolicy getDiscount(int discountId) {
        if (getId() == discountId) {
            return this;
        }
        if (getDiscountPolicies() == null) {
            return null;
        }
        AbstractDiscountPolicy discount = getDiscountPolicies().stream().filter(d -> d.getId() == discountId).findFirst().orElse(null);
        if (discount != null) {
            return discount;
        }
        for (AbstractDiscountPolicy adp : getDiscountPolicies()) {
            AbstractDiscountPolicy res = adp.getDiscount(discountId);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public boolean removeDiscount(int discountId) {
        if (getDiscountPolicies() == null) {
            return false;
        }
        AbstractDiscountPolicy discount = getDiscountPolicies().stream().filter(d -> d.getId() == discountId).findFirst().orElse(null);
        if (discount != null) {
            getDiscountPolicies().remove(discount);
            return true;
        }
        for (AbstractDiscountPolicy adp : getDiscountPolicies()) {
            boolean removed = adp.removeDiscount(discountId);
            if (removed) {
                return removed;
            }
        }
        return false;
    }

    public synchronized void addAndPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This discount policy does not have a predicate");
    }

    public synchronized void addOrPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This discount policy does not have a predicate");
    }

    public synchronized void addXorPredicate(AbstarctPredicate predicate) {
        throw new IllegalArgumentException("This discount policy does not have a predicate");
    }

    public void setPercentage(double percentage) {
        throw new IllegalArgumentException("This discount policy does not have a percentage field.");
    }

    public abstract DiscountDAL toDAL();

    public void update() {
        if (getId() != 0) {
            DiscountManager.getInstance().addObject(toDAL());
        }
    }
}
