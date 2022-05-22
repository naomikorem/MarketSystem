package DomainLayer.Stores.PurchasePolicy;

import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import java.util.Date;
import java.util.Map;

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
}
