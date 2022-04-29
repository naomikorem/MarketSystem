package acceptenceTests;

import DomainLayer.Stores.StoreController;
import DomainLayer.Users.AdminController;
import DomainLayer.Users.SubscribedState;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractTest {
    protected Bridge bridge;

    public AbstractTest() {
        this.bridge = new Proxy(new Real());
    }

    public void before() {
        UserController uMock = mock(UserController.class);
        MockedStatic<UserController> uc = Mockito.mockStatic(UserController.class);
        uc.when(UserController::getInstance).thenReturn(uMock);

        StoreController sMock = mock(StoreController.class);
        MockedStatic<StoreController> sc = Mockito.mockStatic(StoreController.class);
        sc.when(StoreController::getInstance).thenReturn(sMock);

        AdminController aMock = mock(AdminController.class);
        MockedStatic<AdminController> ac = Mockito.mockStatic(AdminController.class);
        ac.when(AdminController::getInstance).thenReturn(aMock);
    }
}
