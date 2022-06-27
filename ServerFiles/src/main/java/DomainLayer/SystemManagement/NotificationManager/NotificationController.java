package DomainLayer.SystemManagement.NotificationManager;

import DataLayer.DALObjects.NotificationDAL;
import DataLayer.DALObjects.NotificationsKey;
import DataLayer.NotificationsManager;
import DomainLayer.Observable;
import DomainLayer.Observer;
import DomainLayer.Users.UserController;
import Utility.LogUtility;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationController implements Observable
{
    public static final String NOTIFICATION_FILE_NAME = "real_time_notifications.txt";
    private Map<String, List<INotification>> users_messages;
    private List<Observer> observers;
    private UserController userController;
    private NotificationsManager manager;

    private static class NotificationControllerHolder {
        static final NotificationController INSTANCE = new NotificationController();
    }
    private NotificationController()
    {
        this.users_messages = new HashMap<>();
        this.userController = UserController.getInstance();
        this.observers = new LinkedList<>();
        this.manager = NotificationsManager.getInstance();
    }

    public static NotificationController getInstance() {
        return NotificationControllerHolder.INSTANCE;
    }

    /***
     * Add notification to given user
     * @param username The given user
     * @param message The message of the notification
     * @return
     */
    public boolean addNotification(String username, String message)
    {
        synchronized (this.users_messages) {
            if (!this.users_messages.containsKey(username)) {
                this.users_messages.put(username, new ArrayList<>());
            }
            this.users_messages.get(username).add(new Notification(message));
            this.manager.addNotification(toDAL(username, message));
            LogUtility.info("Added notification to user " + username);
        }
        return true;
    }

    private NotificationDAL toDAL(String username, String message)
    {
        NotificationsKey key = new NotificationsKey(username, message);
        return new NotificationDAL(key);
    }

    private boolean addRealTimeNotification(Observer user, String msg) {
        addNotification(user.getName(), msg);
        user.sendNotification(new Notification(msg));

        return true;
    }

    /***
     * Remove all the notifications of given user
     * @param username The given user
     */
    public void removeUserNotifications(String username)
    {
        synchronized (this.users_messages)
        {
            if(!this.users_messages.containsKey(username))
                throw new IllegalArgumentException("The user doesn't have notifications.");

            this.users_messages.remove(username);
            this.manager.deleteAllUserNotifications(username);
            LogUtility.info("Removed the notifications that where sent to the user " + username);
        }
    }

    /***
     * Receive all the notifications of some user
     * @param username The given user
     * @return List of notifications
     */
    public List<INotification> getUserNotifications(String username)
    {
        synchronized (this.users_messages) {
            if (!this.users_messages.containsKey(username)) {
                LogUtility.warn("Tried to receive notifications of " + username + " but he doesn't have any notifications.");
                throw new IllegalArgumentException("The user doesn't have notifications.");
            }
            LogUtility.info("Received notifications of " + username);
            return this.users_messages.remove(username);
        }
    }

    /***
     * Send notification to store owners after someone purchased items from their store
     * @param stores_and_owners Map of stores with their owners
     * @param username The username that made the last purchase
     */
    public void notifyStoresOwners(Map<Integer, List<String>> stores_and_owners, String username)
    {
        for (Map.Entry<Integer, List<String>> entry: stores_and_owners.entrySet())
        {
            int store_id = entry.getKey();
            List<String> owners = entry.getValue();
            notifyUsers(owners, "The user " + username +
                    " bought items from the store " + store_id + " at " + (new Date()));

            LogUtility.info("Sent notifications to owners of store " + store_id);
        }
    }

    @Override
    public void attachObserver(Observer observer)
    {
        synchronized (this.observers)
        {
            this.observers.add(observer);
            LogUtility.info("Added new observer to observers list");
        }
    }

    @Override
    public void detachObserver(Observer observer)
    {
        synchronized (this.observers)
        {
            this.observers.remove(observer);
            LogUtility.info("Removed observer from observers list");
        }
    }

    @Override
    public boolean notifyUsers(List<String> users_to_notify, String message)
    {
        boolean flag = false;
        for (String username: users_to_notify)
        {
            synchronized (this.observers)
            {
                Observer observer = this.observers.stream().filter(o -> o.getName().equals(username)).findFirst().orElse(null);
                // If the user is observer - logged in - than send real time notification
                if (observer != null) {
                    flag = addRealTimeNotification(observer, message);
                } else {
                    // If the user is not observer - not logged in, save the message for later
                    flag = addNotification(username, message);
                }
            }
        }
        return flag;
    }

    /***
     * Clear all data structures
     * @return true
     */
    public boolean clearNotifications()
    {
        this.users_messages = new ConcurrentHashMap<>();
        this.manager.clearNotifications();
        return true;
    }

    public void loadNotifications()
    {
        List<NotificationDAL> notifications = this.manager.getAllNotifications();
        for(NotificationDAL n : notifications)
        {
            String username = n.getId().getUsername();
            Notification notification_domain = toDomain(n);

            if (!this.users_messages.containsKey(username)) {
                this.users_messages.put(username, new ArrayList<>());
            }

            this.users_messages.get(username).add(notification_domain);
        }
    }

    private Notification toDomain(NotificationDAL n)
    {
        return new Notification(n.getId().getMessage());
    }
}
