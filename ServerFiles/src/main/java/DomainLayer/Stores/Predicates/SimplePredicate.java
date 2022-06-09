package DomainLayer.Stores.Predicates;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimplePredicate extends AbstarctPredicate {

    private int itemId;
    private double minBasket;
    private Category category;
    private int hour;
    private Date date;
    private PredicateType type;
    private String displayString;

    public enum PredicateType {
        True,
        Item,
        Category,
        Basket,
        Hour,
        Date,
    }


    public SimplePredicate(int itemId) {
        this.itemId = itemId;
        displayString = String.format("Only for item with id: %s", itemId);
        this.type = PredicateType.Item;
    }

    public SimplePredicate(int itemId, double minBasket, Category category, int hour, Date date, PredicateType type) {
        this.itemId = itemId;
        this.minBasket = minBasket;
        this.category = category;
        this.hour = hour;
        this.date = date;
        this.type = type;
    }

    public SimplePredicate(Category category) {
        this.category = category;
        displayString = String.format("Only for items of category %s", category);
        this.type = PredicateType.Category;
    }

    public SimplePredicate(int itemId, int hour) {
        this.itemId = itemId;
        this.hour = hour;
        this.type = PredicateType.Hour;
    }

    public SimplePredicate(int itemId, Calendar date) {
        this.type = PredicateType.Date;
        this.itemId = itemId;
        this.date = date.getTime();
    }

    /*
    public SimplePredicate(int itemId, int age, int hour) {
        Calendar rightNow  = GregorianCalendar.getInstance();
        this.ipc = (i) -> i.getId() == itemId;
        this.bpc = (b) -> b.;
        this.tpc = (t) -> rightNow.get(Calendar.HOUR_OF_DAY) < hour;
    }*/ //need to check about the user age

    public SimplePredicate() {
        this.type = PredicateType.True;
    }

    @Override
    public boolean canApply(Item item, ShoppingBasket shoppingBasket) {
        Calendar rightNow  = GregorianCalendar.getInstance();
        switch (this.type) {
            case True:
                return true;
            case Item:
                return item.getId() == this.itemId;
            case Category:
                return item.getCategory().equals(this.category);
            case Basket:
                return shoppingBasket.calculatePrice() >= this.minBasket;
            case Hour:
                return !shoppingBasket.hasItem(itemId) || rightNow.get(Calendar.HOUR_OF_DAY) < hour;
            case Date:
                Calendar date_check = Calendar.getInstance();
                date_check.setTime(date);
                return (!shoppingBasket.hasItem(itemId) || ((rightNow.get(Calendar.MONTH) < (date_check.get(Calendar.MONTH))) ||
                        ((rightNow.get(Calendar.MONTH) == date_check.get(Calendar.MONTH)) &&
                                (rightNow.get(Calendar.DAY_OF_MONTH)<date_check.get(Calendar.DAY_OF_MONTH)))));
            default:
                return false;
        }
    }

    public void setDisplayString(String s) {
        this.displayString = s;
    }

    public String display() {
        return displayString;
    }
}
