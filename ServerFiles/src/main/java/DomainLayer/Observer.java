package DomainLayer;

import DomainLayer.SystemManagement.NotificationManager.INotification;

import java.util.List;

public interface Observer
{
    public String getName();
    public List<INotification> updateNotifications();
    public void sendNotification(INotification notification);
}
