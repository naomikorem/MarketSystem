package DataLayer.DALObjects;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CompositePolicy")
@PrimaryKeyJoinColumn(name = "id")
public class CompositePurchasePolicyDAL extends PurchasePolicyDAL {
    public enum CompositePolicyType {
        Add,
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CompositePolicyType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "PoliciesInComposite",
            joinColumns = {@JoinColumn(name = "compositeId")},
            inverseJoinColumns = {@JoinColumn(name = "policyId")})
    private Set<PurchasePolicyDAL> policies;

    public CompositePolicyType getType() {
        return type;
    }

    public void setType(CompositePolicyType type) {
        this.type = type;
    }

    public Set<PurchasePolicyDAL> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<PurchasePolicyDAL> policies) {
        this.policies = policies;
    }
}
