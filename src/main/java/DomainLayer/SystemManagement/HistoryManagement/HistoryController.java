package DomainLayer.SystemManagement.HistoryManagement;

import DomainLayer.Users.ShoppingBasket;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryController {

    public static final String GUEST_DEFAULT_NAME = "guest";
    private Map<String, History> users_history;
    private Map<Integer, History> store_history;

    private HistoryController()
    {
        this.users_history = new ConcurrentHashMap<>();
        this.store_history = new ConcurrentHashMap<>();
    }

    private static class HistoryControllerHolder {
        static final HistoryController INSTANCE = new HistoryController();
    }

    public static HistoryController getInstance() {
        return HistoryControllerHolder.INSTANCE;
    }

    public void addToUserHistory(String username, List<ShoppingBasket> baskets, Date purchase_date) {
        synchronized (users_history) {
            if (!this.users_history.containsKey(username)) {
                this.users_history.put(username, new History());
            }

            for (ShoppingBasket basket : baskets) {
                this.users_history.get(username).addToHistory(basket.getItemsAndAmounts(), basket.getStoreId(), username, purchase_date);
            }

        }
    }

    public void addToStoreHistory(String buying_username, List<ShoppingBasket> baskets, Date purchase_date) {

        synchronized (store_history) {
            for (ShoppingBasket basket : baskets) {
                int store_id = basket.getStoreId();
                if (!this.store_history.containsKey(store_id)) {
                    this.store_history.put(store_id, new History());
                }

                this.store_history.get(store_id).addToHistory(basket.getItemsAndAmounts(), store_id, buying_username, purchase_date);
            }
        }
    }

    public History getPurchaseHistory(String username)
    {
        synchronized (users_history) {
            if (!this.users_history.containsKey(username)) {
                throw new IllegalArgumentException("User did not buy anything");
            }

            return this.users_history.get(username);
        }
    }

    public synchronized History getStoreHistory(int store_id)
    {
        synchronized (store_history) {
            if (!this.store_history.containsKey(store_id)) {
                throw new IllegalArgumentException("User did not buy anything");
            }

            return this.store_history.get(store_id);
        }

    }

    public boolean clearHistory()
    {
        this.users_history = new ConcurrentHashMap<>();
        this.store_history = new ConcurrentHashMap<>();
        return true;
    }
}