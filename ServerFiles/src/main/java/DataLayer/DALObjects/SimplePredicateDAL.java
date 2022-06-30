package DataLayer.DALObjects;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.Stores.Predicates.AbstarctPredicate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SimplePredicate")
@PrimaryKeyJoinColumn(name = "id")
public class SimplePredicateDAL extends PredicateDAL {
    private int itemId;
    private double minBasket;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category")
    private Category category;
    private int hour;
    private Date date;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private SimplePredicate.PredicateType type;
    private String displayString;
    private int minAmount;
    private int maxAmount;

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

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SimplePredicate.PredicateType getType() {
        return type;
    }

    public void setType(SimplePredicate.PredicateType type) {
        this.type = type;
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
    public AbstarctPredicate toDomain() {
        SimplePredicate sp = new SimplePredicate();
        sp.setId(getId());
        sp.setItemId(getItemId());
        sp.setMinBasket(getMinBasket());
        sp.setCategory(getCategory());
        sp.setHour(getHour());
        sp.setDate(getDate());
        sp.setType(getType());
        sp.setDisplayString(getDisplayString());
        sp.setMinAmount(getMinAmount());
        sp.setMaxAmount(getMaxAmount());
        return sp;
    }
}
