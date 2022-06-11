package DataLayer;

import DataLayer.DALObjects.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Predicates.SimplePredicate;

import java.util.HashMap;
import java.util.HashSet;

public class StoreManager extends DALManager<StoreDAL, Integer> {

    public StoreManager() {
        super(StoreDAL.class);
    }

    public static void main(String[] args) {
        StoreManager sm = new StoreManager();
        PurchasePolicyManager ppm = new PurchasePolicyManager();
        DiscountManager dm = new DiscountManager();
        PredicateManager pm = new PredicateManager();
        ItemManager im = new ItemManager();
        UserManager um = new UserManager();
        PermissionManager pmm = new PermissionManager();



        /*ItemDAL id = new ItemDAL();
        id.setProduct_name("item");
        id.setCategory(Category.Food);
        id.setPrice(11);
        id.setNumberOfRatings(5);
        id.setRate(10);
        im.addObject(id);

        SimplePredicateDAL sppd = new SimplePredicateDAL();
        sppd.setType(SimplePredicate.PredicateType.Item);
        sppd.setItemId(1);
        sppd.setDisplayString("asda");
        pm.addObject(sppd);

        SimplePurchasePolicyDAL spd = new SimplePurchasePolicyDAL();
        spd.setHour(10);
        spd.setPredicate(sppd);
        ppm.addObject(spd);

        CompositePurchasePolicyDAL cpd = new CompositePurchasePolicyDAL();
        cpd.setType(CompositePurchasePolicyDAL.CompositePolicyType.Add);
        ppm.addObject(cpd);


        StoreDAL s = new StoreDAL();
        s.setName("This Store");
        s.setPurchase(cpd);
        s.setFounder("THISNUTS1");
        s.setOpen(false);
        s.setPermanentlyClosed(true);

        s.setItems(new HashMap<>());
        s.getItems().put(id, 1);

        s.setOwners(new HashMap<>());
        s.getOwners().put(um.getObject("myName"), "deezManagers");

        PermissionDAL pd = new PermissionDAL();
        pd.setGivenBy("DEEZPERMISSIONS");
        pd.setPermissionMask((byte) 7);
        pd.setManager(um.getObject("myName"));
        pmm.addObject(pd);

        s.setManagers(new HashSet<>());
        s.getManagers().add(pd);

        sm.addObject(s);*/
        StoreDAL s = sm.getObject(10);
        System.out.println(s);

    }
}
