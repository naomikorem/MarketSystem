package DataLayer.DALObjects;

import DomainLayer.Stores.Predicates.CompositePredicate;

import javax.persistence.*;
import java.util.Set;

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
}
