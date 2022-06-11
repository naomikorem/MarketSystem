package DataLayer.DALObjects;


import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.AddPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.CompositePurchasePolicy;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public AbstractPurchasePolicy toDomain() {
        CompositePurchasePolicy res;
        switch (type) {
            case Add:
                res = new AddPurchasePolicy();
                break;
            default:
                return null;
        }
        res.setId(getId());
        res.setPurchasePolicies(getPolicies().stream().map(PurchasePolicyDAL::toDomain).collect(Collectors.toList()));
        return res;
    }
}
