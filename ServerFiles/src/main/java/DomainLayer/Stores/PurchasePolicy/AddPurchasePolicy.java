package DomainLayer.Stores.PurchasePolicy;

import DataLayer.DALObjects.CompositePurchasePolicyDAL;
import DataLayer.DALObjects.PurchasePolicyDAL;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class AddPurchasePolicy extends CompositePurchasePolicy {
    @Override
    public boolean applyPolicy(ShoppingBasket sb) {
        for (AbstractPurchasePolicy pp : purchasePolicies) {
            if (!pp.applyPolicy(sb)){
                return false;
            }
        }
        return true;
    }

    @Override
    public PurchasePolicyDAL toDAL() {
        CompositePurchasePolicyDAL res = new CompositePurchasePolicyDAL();
        res.setId(getId());
        res.setType(CompositePurchasePolicyDAL.CompositePolicyType.Add);
        res.setPolicies(getPurchasesPolicies().stream().map(AbstractPurchasePolicy::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
