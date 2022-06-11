package DataLayer.DALObjects;

import javax.persistence.*;
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
}
