package DataLayer.DALObjects;


import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
}
