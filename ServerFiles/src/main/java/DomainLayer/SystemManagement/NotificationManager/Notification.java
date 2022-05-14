package DomainLayer.SystemManagement.NotificationManager;

public class Notification implements INotification {

    private String message;
    public Notification(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
