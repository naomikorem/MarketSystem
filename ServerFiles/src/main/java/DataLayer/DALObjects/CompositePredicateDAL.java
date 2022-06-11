package DataLayer.DALObjects;

import DomainLayer.Stores.Predicates.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "CompositePredicates")
@PrimaryKeyJoinColumn(name = "id")
public class CompositePredicateDAL extends PredicateDAL {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CompositePredicate.PredicateEnum type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "PredicatesInComposite",
            joinColumns = {@JoinColumn(name = "CompositeId")},
            inverseJoinColumns = {@JoinColumn(name = "PredicateId")})
    private Set<PredicateDAL> preds;

    public CompositePredicate.PredicateEnum getType() {
        return type;
    }

    public void setType(CompositePredicate.PredicateEnum type) {
        this.type = type;
    }

    public Set<PredicateDAL> getPreds() {
        return preds;
    }

    public void setPreds(Set<PredicateDAL> preds) {
        this.preds = preds;
    }

    @Override
    public AbstarctPredicate toDomain() {
        List<AbstarctPredicate> preds = getPreds().stream().map(PredicateDAL::toDomain).collect(Collectors.toList());
        CompositePredicate res;
        switch (getType()) {
            case AND:
                res = new AndCompositePredicate(preds);
                break;
            case OR:
                res = new OrCompositePredicate(preds);
                break;
            case XOR:
                res = new XorCompositePredicate(preds);
                break;
            case COND:
                res = new CondCompositePredicate(preds);
                break;
            default:
                return null;
        }
        res.setId(getId());
        return res;
    }
}
