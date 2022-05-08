package DomainLayer.SystemManagement.NotificationManager;

import Utility.LogUtility;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationController
{
    private Map<String, List<INotification>> users_messages;

    private static class NotificationControllerHolder {
        static final NotificationController INSTANCE = new NotificationController();
    }
    private NotificationController()
    {
        this.users_messages = new ConcurrentHashMap<>();
    }

    public static NotificationController getInstance() {
        return NotificationControllerHolder.INSTANCE;
    }

    /***
     * Add notification to given user
     * @param username The given user
     * @param message The message of the notification
     */
    public synchronized void addNotification(String username, String message)
    {
        if(!this.users_messages.containsKey(username))
        {
            this.users_messages.put(username, new CopyOnWriteArrayList<>());
        }
        this.users_messages.get(username).add(new Notification(message));
        LogUtility.info("Added notification to user " + username);
    }

    /***
     * Remove all the notifications of given user
     * @param username The given user
     */
    public synchronized void removeUserNotifications(String username)
    {
        if(!this.users_messages.containsKey(username))
            throw new IllegalArgumentException("The user doesn't have notifications.");

        this.users_messages.remove(username);
        LogUtility.info("Removed the notifications that where sent to the user " + username);
    }

    /***
     * Receive all the notifications of some user
     * @param username The given user
     * @return List of notifications
     */
    public synchronized List<INotification> getUserNotifications(String username)
    {
        if(!this.users_messages.containsKey(username))
        {
            LogUtility.warn("Tried to receive notifications of " + username + " but he doesn't have any notifications.");
            throw new IllegalArgumentException("The user doesn't have notifications.");
        }
        LogUtility.info("Received notifications of " + username);
        return this.users_messages.get(username);
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
            owners.forEach(owner -> this.addNotification(owner, "The user " + username +
                                    " bought items from the store " + store_id + " at " + (new Date())));
        }
        LogUtility.info("Sent notifications to owners of stores " + stores_and_owners.keySet());
    }

    /***
     * Clear all data structures
     * @return true
     */
    public boolean clearNotifications()
    {
        this.users_messages = new ConcurrentHashMap<>();
        return true;
    }
}
