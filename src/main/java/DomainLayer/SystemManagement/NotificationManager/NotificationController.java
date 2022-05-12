package DomainLayer.SystemManagement.NotificationManager;

import DomainLayer.Observable;
import DomainLayer.Observer;
import DomainLayer.Users.UserController;
import Utility.LogUtility;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class NotificationController implements Observable
{
    public static final String NOTIFICATION_FILE_NAME = "real_time_notifications.txt";
    private Map<String, List<INotification>> users_messages;
    private Map<String, List<INotification>> real_time_users_messages; // just for check
    private List<Observer> observers;
    private UserController userController;

    private static class NotificationControllerHolder {
        static final NotificationController INSTANCE = new NotificationController();
    }
    private NotificationController()
    {
        this.real_time_users_messages = new HashMap<>();
        this.users_messages = new HashMap<>();
        this.userController = UserController.getInstance();
        this.observers = new LinkedList<>();
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
            LogUtility.info("Added notification to user " + username);
        }
        return true;
    }

    private boolean addRealTimeNotification(String username, String msg)
    {
        synchronized (this.real_time_users_messages) {
            if (!this.real_time_users_messages.containsKey(username)) {
                this.real_time_users_messages.put(username, new ArrayList<>());
            }
            this.real_time_users_messages.get(username).add(new Notification(msg));
            LogUtility.info("Added real time notification to user " + username);
        }
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
            synchronized (this.real_time_users_messages)
            {
                if(!this.users_messages.containsKey(username) || !this.real_time_users_messages.containsKey(username))
                    throw new IllegalArgumentException("The user doesn't have notifications.");

                this.users_messages.remove(username);
                this.real_time_users_messages.remove(username);
                LogUtility.info("Removed the notifications that where sent to the user " + username);
            }
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
            return this.users_messages.get(username);
        }
    }

    /**
     * The function will be deleted after adding sockets
     * */
    public List<INotification> getUserRealTimeNotifications(String username)
    {
        synchronized (this.real_time_users_messages) {
            if (!this.real_time_users_messages.containsKey(username)) {
                LogUtility.warn("Tried to receive real time notifications of " + username + " but he doesn't have any notifications.");
                throw new IllegalArgumentException("The user doesn't have real time notifications.");
            }
            LogUtility.info("Received real time notifications of " + username);
            return this.real_time_users_messages.get(username);
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
                // If the user is observer - logged in - than send real time notification
                if (this.observers.stream().map(Observer::getName).collect(Collectors.toList()).contains(username)) {
                    flag = addRealTimeNotification(username, message);
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
        this.real_time_users_messages = new ConcurrentHashMap<>();
        return true;
    }
}
