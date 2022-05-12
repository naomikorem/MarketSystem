package DomainLayer.Stores.Predicates;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimplePredicate extends AbstarctPredicate {

    public interface ItemPredicateCallback {
        public boolean checkItem(Item i);
    }

    public interface BasketPredicateCallback {
        public boolean checkBasket(ShoppingBasket i);
    }


    private ItemPredicateCallback ipc;
    private BasketPredicateCallback bpc;

    public SimplePredicate(ItemPredicateCallback ipc, BasketPredicateCallback bpc) {
        this.ipc = ipc;
        this.bpc = bpc;
    }

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

    public SimplePredicate(int itemId, int hour) {
        this.ipc = (i) -> true;
        this.bpc = (b) -> {
            Calendar rightNow  = GregorianCalendar.getInstance();
            return !b.hasItem(itemId) || rightNow.get(Calendar.HOUR_OF_DAY) < hour;
        };
    }

    public SimplePredicate(int itemId, Calendar date) {
        this.ipc = (i) -> true;
        this.bpc = (b) -> {
            Calendar rightNow  = GregorianCalendar.getInstance();
            return !b.hasItem(itemId) || ((rightNow.get(Calendar.MONTH) < date.get(Calendar.MONTH)) ||
                    ((rightNow.get(Calendar.MONTH) == date.get(Calendar.MONTH)) &&
                    (rightNow.get(Calendar.DAY_OF_MONTH)<date.get(Calendar.DAY_OF_MONTH))));
        };
    }

    /*
    public SimplePredicate(int itemId, int age, int hour) {
        Calendar rightNow  = GregorianCalendar.getInstance();
        this.ipc = (i) -> i.getId() == itemId;
        this.bpc = (b) -> b.;
        this.tpc = (t) -> rightNow.get(Calendar.HOUR_OF_DAY) < hour;
    }*/ //need to check about the user age

    public SimplePredicate() {
        this.ipc = (i) -> true;
        this.bpc = (b) -> true;
    }

    @Override
    public boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        return ipc.checkItem(item) && bpc.checkBasket(shoppingBasket);
    }
}
