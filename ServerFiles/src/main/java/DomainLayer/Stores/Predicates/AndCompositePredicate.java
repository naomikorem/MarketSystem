package DomainLayer.Stores.Predicates;

import DataLayer.DALObjects.CompositePredicateDAL;
import DataLayer.DALObjects.PredicateDAL;

import java.util.List;
import java.util.stream.Collectors;

public class AndCompositePredicate extends CompositePredicate {

    public AndCompositePredicate() {
        super();
    }
    public AndCompositePredicate(List<AbstarctPredicate> preds) {
        super(preds);
    }

    @Override
    protected boolean applyOperation(boolean pred1, boolean pred2) {
        return (pred1 && pred2);
    }

    @Override
    public String display() {
        if (this.preds == null || this.preds.isEmpty()) {
            return "true";
        }
        if (this.preds.size() == 1) {
            return this.preds.get(0).display();
        }
        return this.preds.stream().map(AbstarctPredicate::display).collect(Collectors.joining(" and "));
    }

    @Override
    public PredicateDAL toDAL() {
        CompositePredicateDAL res = new CompositePredicateDAL();
        res.setId(getId());
        res.setType(PredicateEnum.AND);
        res.setPreds(preds.stream().map(AbstarctPredicate::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
