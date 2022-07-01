package DomainLayer.Stores.Predicates;

import DataLayer.DALObjects.PredicateDAL;
import DataLayer.DALObjects.SimplePredicateDAL;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class SimplePredicate extends AbstarctPredicate {

    private int itemId;
    private double minBasket;
    private Category category;
    private int hour;
    private Date date;
    private PredicateType type;
    private String displayString;
    private int minAmount;
    private int maxAmount;

    public enum PredicateType {
        True,
        Item,
        Category,
        Basket,
        Hour,
        Date,
        LimitItem,
    }

    public SimplePredicate(int itemId, int min, int max) {
        this.itemId = itemId;
        this.minAmount = min;
        this.maxAmount = max;
        this.type = PredicateType.LimitItem;
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
        Calendar rightNow  = Calendar.getInstance();
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

            case LimitItem:
                Map.Entry<Item, Integer> itemAmount = shoppingBasket.getItemsAndAmounts().stream().filter(i -> i.getKey().getId() == itemId).findFirst().orElse(null);
                if (itemAmount == null) {
                    return true;
                }
                return itemAmount.getValue() >= getMinAmount() && itemAmount.getValue() <= getMaxAmount();
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getMinBasket() {
        return minBasket;
    }

    public void setMinBasket(double minBasket) {
        this.minBasket = minBasket;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PredicateType getType() {
        return type;
    }

    public void setType(PredicateType type) {
        this.type = type;
    }

    public String getDisplayString() {
        return displayString;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public PredicateDAL toDAL() {
        SimplePredicateDAL res = new SimplePredicateDAL();
        res.setId(getId());
        res.setDisplayString(getDisplayString());
        res.setDate(getDate());
        res.setCategory(getCategory());
        res.setType(getType());
        res.setItemId(getItemId());
        res.setMinBasket(getMinBasket());
        res.setMinAmount(getMinAmount());
        res.setMaxAmount(getMaxAmount());
        return res;
    }
}
