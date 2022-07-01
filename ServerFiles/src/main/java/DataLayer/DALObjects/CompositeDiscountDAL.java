package DataLayer.DALObjects;


import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.AddDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.CompositeDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.MaxDiscountPolicy;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "CompositeDiscount")
@PrimaryKeyJoinColumn(name = "id")
public class CompositeDiscountDAL extends DiscountDAL {

    public enum CompositeDiscountType {
        Add,
        Max,
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CompositeDiscountType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "DiscountsInComposite",
            joinColumns = {@JoinColumn(name = "compositeId")},
            inverseJoinColumns = {@JoinColumn(name = "discountId")})
    private Set<DiscountDAL> discountPolicies;

    public CompositeDiscountType getType() {
        return type;
    }

    public void setType(CompositeDiscountType type) {
        this.type = type;
    }

    public Set<DiscountDAL> getDiscountPolicies() {
        return discountPolicies;
    }

    public void setDiscountPolicies(Set<DiscountDAL> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public AbstractDiscountPolicy toDomain() {
        CompositeDiscountPolicy res;
        List<AbstractDiscountPolicy> policies = getDiscountPolicies().stream().map(DiscountDAL::toDomain).collect(Collectors.toList());
        switch (getType()) {
            case Add:
                res = new AddDiscountPolicy();
                break;
            case Max:
                res = new MaxDiscountPolicy();
                break;
            default:
                return null;
        }
        res.setDiscountPolicies(policies);
        res.setId(getId());
        return res;
    }
}
