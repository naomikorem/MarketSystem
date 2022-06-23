package DomainLayer.SystemManagement.HistoryManagement;

import DataLayer.DALObjects.HistoryItemDAL;
import DataLayer.HistoryManager;
import DomainLayer.Users.ShoppingBasket;
import Utility.LogUtility;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryController
{
    public static final String GUEST_DEFAULT_NAME = "guest";
    private Map<String, History> users_history;
    private Map<Integer, History> store_history;
    private HistoryManager manager;

    private HistoryController()
    {
        this.users_history = new ConcurrentHashMap<>();
        this.store_history = new ConcurrentHashMap<>();
        this.manager = HistoryManager.getInstance();
    }

    private static class HistoryControllerHolder {
        static final HistoryController INSTANCE = new HistoryController();
    }

    public static HistoryController getInstance() {
        return HistoryControllerHolder.INSTANCE;
    }

    /***
     * Adding items to the user's personal purchase history.
     * @param username The username of the user that purchased his shopping cart.
     * @param baskets The baskets of the user
     * @param purchase_date The date of the user's purchase.
     */
    public void addToUserHistory(String username, List<ShoppingBasket> baskets, Date purchase_date) {
        synchronized (users_history)
        {
            if (!this.users_history.containsKey(username))
            {
                this.users_history.put(username, new History());
            }

            for (ShoppingBasket basket : baskets)
            {
                this.users_history.get(username).addToHistory(basket.getItemsAndAmounts(), basket.getStoreId(), username, purchase_date);
            }

            LogUtility.info("Added history to user's " + username + " personal purchase history");
        }
    }

    /***
     * Adding the items that user purchased to the matching store purchase history
     * @param buying_username The username of the user that made the purchase
     * @param baskets The user's baskets
     * @param purchase_date The date of the purchase
     */
    public void addToStoreHistory(String buying_username, List<ShoppingBasket> baskets, Date purchase_date) {

        synchronized (store_history)
        {
            for (ShoppingBasket basket : baskets)
            {
                int store_id = basket.getStoreId();
                if (!this.store_history.containsKey(store_id))
                {
                    this.store_history.put(store_id, new History());
                }

                List<ItemHistory> added_items = this.store_history.get(store_id).addToHistory(basket.getItemsAndAmounts(), store_id, buying_username, purchase_date);
                for(ItemHistory item_domain : added_items)
                {
                    this.manager.addItemToHistory(toDAL(item_domain));
                }

                //added_items.stream().map(hi-> this.manager.addItemToHistory(toDAL(hi)));

                LogUtility.info("Added history to store " + store_id + " history by " + buying_username);
            }
        }
    }

    /***
     * Receive the purchase history of a subscribed user.
     * @param username The name of the requested user
     * @return History of the given user - if found in users_history
     */
    public History getPurchaseHistory(String username)
    {
        synchronized (users_history)
        {
            if (!this.users_history.containsKey(username))
            {
                throw new IllegalArgumentException("User did not buy anything");
            }
            return this.users_history.get(username);
        }
    }

    /***
     * Receive the purchase history of a given store
     * @param store_id The requested store
     * @return History of the relevant store, if exists in store_history
     */
    public History getStoreHistory(int store_id)
    {
        synchronized (store_history)
        {
            if (!this.store_history.containsKey(store_id))
            {
                throw new IllegalArgumentException("No one bought anything from the store yet");
            }
            return this.store_history.get(store_id);
        }
    }

    /***
     * Clear all the history from the data structures
     * @return true
     */
    public boolean clearHistory()
    {
        this.users_history = new ConcurrentHashMap<>();
        this.store_history = new ConcurrentHashMap<>();
        this.manager.clearTable();
        return true;
    }

    public void loadHistory()
    {
        List< HistoryItemDAL> history_items_dal = this.manager.getAllHistoryItems();
        for (HistoryItemDAL item_dal: history_items_dal)
        {
            ItemHistory item_history_domain = toDomain(item_dal);
            String username = item_history_domain.username;
            int store_id = item_history_domain.store_id;

            if (!this.store_history.containsKey(store_id))
            {
                this.store_history.put(store_id, new History());
            }
            this.store_history.get(store_id).addItemHistory(item_history_domain);

            if (!this.users_history.containsKey(username))
            {
                this.users_history.put(username, new History());
            }
            this.users_history.get(username).addItemHistory(item_history_domain);
        }
    }

    private ItemHistory toDomain(HistoryItemDAL item_dal)
    {
        return new ItemHistory(item_dal.getItemId(), item_dal.getStore_id(), item_dal.getUsername(),
                item_dal.getProduct_name(), item_dal.getCategory(), item_dal.getPrice_per_unit(),
                item_dal.getAmount(), item_dal.getDate());
    }

    private HistoryItemDAL toDAL(ItemHistory history_item)
    {
        HistoryItemDAL dal_item = new HistoryItemDAL();
        dal_item.setItemId(history_item.id);
        dal_item.setUsername(history_item.username);
        dal_item.setStore_id(history_item.store_id);
        dal_item.setProduct_name(history_item.product_name);
        dal_item.setCategory(history_item.category);
        dal_item.setPrice_per_unit(history_item.price_per_unit);
        dal_item.setAmount(history_item.amount);
        dal_item.setDate(history_item.date);
        return dal_item;
    }
}