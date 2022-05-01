package acceptenceTests;

import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.HistoryManagement.HistoryController;

import DomainLayer.SystemManagement.MarketManagementFacade;

import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.AdminController;
import DomainLayer.Users.UserController;

public abstract class AbstractTest {
    protected Bridge bridge;


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
}
