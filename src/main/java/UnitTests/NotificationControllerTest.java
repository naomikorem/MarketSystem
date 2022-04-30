package UnitTests;

import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import acceptenceTests.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class NotificationControllerTest extends AbstractTest {
    //private final NotificationController notificationController;

    private final String username1;
    private final String username2;
    private final String message;
    private final String message2;
    private final String message3;

    public NotificationControllerTest()
    {
        //this.notificationController = NotificationController.getInstance();
        this.username1 = "some name";
        this.username2 = "another name";
        this.message = "notification message!!!";
        this.message2 = "another notification message!!!";
        this.message3 = "message 3";
    }

    @Before
    public void setup()
    {
        //super.before();
    }

    @After
    public void after()
    {
        //super.unmock();
    }

    @Test
    public void addNotificationTest(){
        final NotificationController notificationController = NotificationController.getInstance();
        notificationController.addNotification(username1, message);
        notificationController.addNotification(username1, message2);

        notificationController.addNotification(username2, message3);

        List<INotification> user_notifications = notificationController.getUserNotifications(username1);
        List<String> user_messages = user_notifications.stream().map(INotification::getMessage).collect(Collectors.toList());

        assertEquals(2, user_notifications.size());
        assertTrue(user_messages.contains(message));
        assertTrue(user_messages.contains(message2));

        List<INotification> user2_notifications = notificationController.getUserNotifications(username2);
        assertEquals(1, user2_notifications.size());
        assertEquals(user2_notifications.get(0).getMessage(), message3);
    }

    @Test
    public void removeNotifications(){
        final NotificationController notificationController = NotificationController.getInstance();
        notificationController.addNotification(username1, message);
        notificationController.addNotification(username1, message3);

        notificationController.addNotification(username2, message2);

        List<INotification> user_notifications = notificationController.getUserNotifications(username1);
        assertEquals(2, user_notifications.size());

        List<INotification> user2_notifications = notificationController.getUserNotifications(username2);
        assertEquals(1, user2_notifications.size());

        notificationController.removeUserNotifications(username1);

        assertThrows(IllegalArgumentException.class, () -> notificationController.getUserNotifications(username1));

        user2_notifications = notificationController.getUserNotifications(username2);
        assertEquals(1, user2_notifications.size());
    }
}
