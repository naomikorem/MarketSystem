package acceptenceTests;

import DomainLayer.Stores.Store;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.AdminController;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.junit.After;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractTest {
    protected Bridge bridge;
    private MockedStatic<UserController> uc;
    private MockedStatic<StoreController> sc;
    private MockedStatic<AdminController> ac;
    private MockedStatic<NotificationController> nc;


    public AbstractTest() {
        this.mock();
        this.bridge = new Proxy(new Real());
    }

    public void mock() {
        Mockito.reset();
        UserController uMock = new UserController();
        uc = Mockito.mockStatic(UserController.class);
        uc.when(UserController::getInstance).thenReturn(uMock);

        StoreController sMock = new StoreController();
        sc = Mockito.mockStatic(StoreController.class);
        sc.when(StoreController::getInstance).thenReturn(sMock);

        AdminController aMock = new AdminController();
        ac = Mockito.mockStatic(AdminController.class);
        ac.when(AdminController::getInstance).thenReturn(aMock);

        NotificationController nMock = new NotificationController();
        nc = Mockito.mockStatic(NotificationController.class);
        nc.when(NotificationController::getInstance).thenReturn(nMock);
    }

    @After
    public void unmock() {
        uc.close();
        sc.close();
        ac.close();
        nc.close();
    }
}
