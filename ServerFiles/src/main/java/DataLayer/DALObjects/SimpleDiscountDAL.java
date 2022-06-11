package DataLayer.DALObjects;

import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Predicates.SimplePredicate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "simpleDiscount")
@PrimaryKeyJoinColumn(name = "id")
public class SimpleDiscountDAL extends DiscountDAL {
    private double percentage;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "predicateId")
    private PredicateDAL predicate;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public PredicateDAL getPredicate() {
        return predicate;
    }

    public void setPredicate(PredicateDAL predicate) {
        this.predicate = predicate;
    }

    @Override
    public AbstractDiscountPolicy toDomain() {
        SimpleDiscountPolicy sdp = new SimpleDiscountPolicy(getPercentage(), getPredicate() == null ? null : getPredicate().toDomain());
        sdp.setId(getId());
        return sdp;
    }
}
