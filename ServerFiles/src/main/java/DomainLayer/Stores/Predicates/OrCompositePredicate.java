package DomainLayer.Stores.Predicates;

import DataLayer.DALObjects.CompositePredicateDAL;
import DataLayer.DALObjects.PredicateDAL;

import java.util.List;
import java.util.stream.Collectors;

public class OrCompositePredicate extends CompositePredicate {
    public OrCompositePredicate() {
        super();
    }

    public OrCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 || pred2);
    }

    @Override
    public String display() {
        if (this.preds == null || this.preds.isEmpty()) {
            return "true";
        }
        if (this.preds.size() == 1) {
            return this.preds.get(0).display();
        }
        return this.preds.stream().map(p -> String.format("(%s)", p.display())).collect(Collectors.joining(" or "));
    }

    @Override
    public PredicateDAL toDAL() {
        CompositePredicateDAL res = new CompositePredicateDAL();
        res.setId(getId());
        res.setType(PredicateEnum.OR);
        res.setPreds(preds.stream().map(AbstarctPredicate::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
