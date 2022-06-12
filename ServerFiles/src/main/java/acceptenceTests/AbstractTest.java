package acceptenceTests;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.StoreController;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.MarketManagementFacade;

import DomainLayer.Users.AdminController;
import DomainLayer.Users.UserController;

import java.util.Set;

public abstract class AbstractTest {
    protected Bridge bridge;
    protected String DEFAULT_PAYMENT_NAME = AbstractProxy.WSEP_PAYMENT;
    protected String DEFAULT_SUPPLY_NAME = AbstractProxy.WSEP_SUPPLY;


    public AbstractTest() {
        this.clearAll();
        this.bridge = new Proxy(new Real());
    }

    public void clearAll() {
        UserController.getInstance().clearAll();
        StoreController.getInstance().clearAll();
        AdminController.getInstance().clearAll();
        MarketManagementFacade.getInstance().clearAll();
    }

    public boolean compareHistoryItemsToRegularItems(Set<ItemHistory> history_items, Set<Item> regular_items)
    {
        if(history_items.size() != regular_items.size())
        {
            return false;
        }

        for(ItemHistory history_item : history_items)
        {
            boolean found_history_item = false;
            for(Item regular : regular_items)
            {
                if(compare(regular, history_item))
                {
                    found_history_item = true;
                    break;
                }
            }
            if(!found_history_item)
                return false;
        }
        return true;
    }

    protected void addStubs()
    {
        bridge.login(UserController.DEFAULT_ADMIN_USER, UserController.DEFAULT_ADMIN_PASSWORD);
        bridge.addExternalSupplyService(AbstractProxy.GOOD_STUB_NAME, "url");
        bridge.addExternalPurchaseService(AbstractProxy.GOOD_STUB_NAME, "url");
        bridge.logout();
    }

    public boolean compare(Item item, ItemHistory itemHistory)
    {
        return item.getPrice() == itemHistory.price_per_unit &&
                item.getProductName().equals(itemHistory.product_name) &&
                item.getId() == itemHistory.id &&
                item.getCategory() == itemHistory.category;
        //item.get.store_id == this.store_id &&
        //item.amount == this.amount &&
    }
}
