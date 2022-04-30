package DomainLayer.SystemManagement.NotificationManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationController {
    private final Map<String, List<INotification>> users_messages;
    private static class NotificationControllerHolder {
        static final NotificationController INSTANCE = new NotificationController();
    }
    public NotificationController()
    {
        this.users_messages = new ConcurrentHashMap<>();
    }

    public static NotificationController getInstance() {
        return NotificationControllerHolder.INSTANCE;
    }

    public void addNotification(String username, String message)
    {
        if(!this.users_messages.containsKey(username))
        {
            this.users_messages.put(username, new CopyOnWriteArrayList<>());
        }

        this.users_messages.get(username).add(new Notification(message));
    }

    public void removeUserNotifications(String username)
    {
        if(!this.users_messages.containsKey(username))
            throw new IllegalArgumentException("The user doesn't have notifications.");

        this.users_messages.remove(username);
    }

    public List<INotification> getUserNotifications(String username)
    {
        if(!this.users_messages.containsKey(username))
            throw new IllegalArgumentException("The user doesn't have notifications.");

        return this.users_messages.get(username);
    }
}
