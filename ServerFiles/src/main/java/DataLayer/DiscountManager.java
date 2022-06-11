package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Predicates.SimplePredicate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DiscountManager extends DALManager<DiscountDAL, Integer> {
    public DiscountManager() {
        super(DiscountDAL.class);
    }

    public static void main(String[] args) {
        /*SimpleDiscountDAL s = new SimpleDiscountDAL();
        s.setPercentage(10);


        (new DiscountManager()).addObject(s);

        CompositeDiscountDAL c = new CompositeDiscountDAL();
        c.setType(CompositeDiscountDAL.CompositeDiscountType.Max);
        c.setDiscountPolicies(new HashSet<>());
        c.getDiscountPolicies().add(s);

        (new DiscountManager()).addObject(c);*/

        /*DiscountDAL d = (new DiscountManager()).getObject(5);
        System.out.println(d);*/

        /*SimplePredicateDAL p =new SimplePredicateDAL();
        p.setType(SimplePredicate.PredicateType.Item);
        p.setItemId(1);
        p.setDisplayString("here");
        (new PredicateManager()).addObject(p);

        SimpleDiscountDAL s = new SimpleDiscountDAL();
        s.setPercentage(10);
        s.setPredicate(p);

        (new DiscountManager()).addObject(s);*/

        DiscountDAL d = (new DiscountManager()).getObject(6);
        System.out.println(d);
    }
}
