package DataLayer.DALObjects;

import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "SimplePurchasePolicy")
@PrimaryKeyJoinColumn(name = "id")
public class SimplePurchasePolicyDAL extends PurchasePolicyDAL {
    private int hour;
    private Date date;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "predicateId")
    private PredicateDAL predicate;

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

    public PredicateDAL getPredicate() {
        return predicate;
    }

    public void setPredicate(PredicateDAL predicate) {
        this.predicate = predicate;
    }

    @Override
    public AbstractPurchasePolicy toDomain() {
        SimplePurchasePolicy sp = new SimplePurchasePolicy(getPredicate() == null ? null : getPredicate().toDomain());

        if (getDate() == null) {
            sp.setDate(null);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            sp.setDate(calendar);
        }
        sp.setHour(getHour());
        sp.setId(getId());
        return sp;
    }
}
