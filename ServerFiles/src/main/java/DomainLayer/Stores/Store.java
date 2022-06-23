package DomainLayer.Stores;

import DataLayer.*;
import DataLayer.DALObjects.*;
import DomainLayer.Stores.DiscountPolicy.*;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.AddPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.CompositePurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import Exceptions.LogException;
import Utility.LogUtility;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Store {
    private String name;
    private boolean open;
    private int id;
    private String founder;
    private Map<User, String> owners;
    private Map<User, Permission> managers;
    private Map<Item, Integer> items;
    private final Lock lock;
    private final Lock discountLock;
    private final Lock purchaseLock;
    private boolean permanentlyClosed;
    private CompositeDiscountPolicy discountPolicy;
    private CompositePurchasePolicy purchasePolicy;
    private Map<Integer ,Bid> bids;

    public Store(String name, boolean open, int id, String founder, Map<User, String> owners, Map<User, Permission> managers, Map<Item, Integer> items, Lock lock, Lock discountLock, Lock purchaseLock, boolean permanentlyClosed, CompositeDiscountPolicy discountPolicy, CompositePurchasePolicy purchasePolicy, Map<Integer, Bid> bids) {
        this.name = name;
        this.open = open;
        this.id = id;
        this.founder = founder;
        this.owners = owners;
        this.managers = managers;
        this.items = items;
        this.lock = lock;
        this.discountLock = discountLock;
        this.purchaseLock = purchaseLock;
        this.permanentlyClosed = permanentlyClosed;
        this.discountPolicy = discountPolicy;
        this.purchasePolicy = purchasePolicy;
        this.bids = bids;
    }

    public Store(User founder, String store_name, int id) {
        this.founder = founder.getName();
        this.open = true;
        this.permanentlyClosed = false;
        this.id = id;
        this.owners = new HashMap<>();
        this.managers = new HashMap<>();
        this.items = new HashMap<>();
        this.lock = new ReentrantLock();
        this.discountLock = new ReentrantLock();
        this.purchaseLock = new ReentrantLock();
        owners.put(founder, null);
        setName(store_name);
        this.bids = new HashMap<>();
    }


    public Store(String founder, boolean open, boolean permanentlyClosed, Map<User, String> owners, Map<User, Permission> managers, Map<Item, Integer> items, String store_name, int id, CompositeDiscountPolicy cdp, CompositePurchasePolicy cpp) {
        this.founder = founder;
        this.open = open;
        this.permanentlyClosed = permanentlyClosed;
        this.id = id;
        this.owners = owners;
        this.managers = managers;
        this.items = items;
        this.lock = new ReentrantLock();
        this.discountLock = new ReentrantLock();
        this.purchaseLock = new ReentrantLock();
        this.name = store_name;
        this.discountPolicy = cdp;
        this.purchasePolicy = cpp;
        this.bids = new HashMap<>();
    }

    public boolean isOwner(User u) {
        return this.owners.containsKey(u);
    }

    public boolean isOwner(String name) {
        return owners.entrySet().stream().filter(e -> e.getKey().getName().equals(name)).findFirst().orElse(null) != null;
    }

    public boolean isManager(User user) {
        return managers.containsKey(user);
    }

    public boolean isManager(String name) {
        return managers.entrySet().stream().filter(e -> e.getKey().getName().equals(name)).findFirst().orElse(null) != null;
    }

    public boolean canManageItems(User user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canChangeItems());
    }

    public String getName() {
        return name;
    }

    public void setName(String store_name) {
        if (!isOpen()) {
            LogUtility.error("tried to change store name for a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        if (store_name == null || store_name.equals("")) {
            LogUtility.warn("tried to change store name to an empty word / null");
            throw new IllegalArgumentException("Store name must be a non empty name");
        }
        this.name = store_name;
    }

    public boolean isOpen() {
        return open && !permanentlyClosed;
    }

    public boolean isPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setIsOpen(String username, boolean is_open) {
        if (!this.founder.equals(username)) {
            LogUtility.error(String.format("someone who isn't the store founder tried to close the store store id %s", getStoreId()));
            throw new IllegalArgumentException("someone who isn't the store founder tried to close the store");
        }
        this.open = is_open;
    }

    public void setPermanentlyClosed(boolean closed) {
        this.permanentlyClosed = closed;
    }

    public int getStoreId() {
        return id;
    }

    public boolean canBecomeManager(User user) {
        return !managers.containsKey(user);
    }

    public boolean canAssignManager(User user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canAssignManager());
    }

    public boolean canBecomeOwner(User user) {
        return !owners.containsKey(user);
    }

    public boolean canAssignOwner(String user) {
        return isOwner(user);
    }

    public void addManager(String givenBy, User manager) {
        synchronized (lock) {
            saveUpdateState(() -> {
                if (!isOpen()) {
                    LogUtility.error("tried to add a manger for a closed store");
                    throw new IllegalArgumentException("This store is closed");
                }
                if (managers.containsKey(manager) || owners.containsKey(manager)) {
                    LogUtility.error("tried to add a manger who is already a store-owner/ manager");
                    throw new IllegalArgumentException("This manger is already a store-owner/ manager");
                }
                Permission p = new Permission(givenBy);
                p.setId(PermissionManager.getInstance().addObject(p.toDAL(manager)));
                this.managers.put(manager, p);
                manager.addManagedStore(getStoreId());
                LogUtility.info("New manager for shop " + this.id + ", the store manger is :" + manager.getName());
            });
        }
    }

    public void addOwner(String givenBy, User newOwner) {
        synchronized (lock) {
            saveUpdateState(() -> {
                if (!isOpen()) {
                    LogUtility.error("tried to add owner for a closed store");
                    throw new IllegalArgumentException("This store is closed");
                }
                if (managers.containsKey(newOwner) || owners.containsKey(newOwner)) {
                    LogUtility.error("tried to add a manger who is already a store-owner/ manager");
                    throw new IllegalArgumentException("This manger is already a store-owner/ manager");
                }
                this.owners.put(newOwner, givenBy);
                newOwner.addOwnedStore(getStoreId());
                LogUtility.info("New store owner for shop " + this.id + ", the store owner is :" + newOwner.getName());
            });
        }
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public Set<Item> getItemsWithNameContains(String name) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.isNameContains(name)).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithKeyWords(List<String> kws) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.hasKeyWords(kws)).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithCategory(Category category) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter((x) -> x.getCategory().equals(category)).collect(Collectors.toSet());
    }

    public Item getItemById(int id) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        return items.keySet().stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public Item getItemAndDeduct(int itemId, int toDeduct) {
        if (!isOpen()) {
            LogUtility.error("tried to get item from a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
//        if(toDeduct == 0)
//        {
//            throw new LogException("You can't add 0 items to your shopping cart", "User tried to buy 0 items from item " + itemId);
//        }
        synchronized (items) {
            Item i = items.keySet().stream().filter(item -> item.getId() == itemId).findFirst().orElse(null);
            if (i != null) {
                int amount = items.getOrDefault(i, 0);
                if (amount >= toDeduct) {
                    items.put(i, amount - toDeduct);
                    try {
                        update();
                    } catch (Exception e) {
                        items.put(i, amount);
                        throw e;
                    }
                    return i;
                } else {
                    throw new LogException("There isn't enough item " + itemId + " in stock", "User tried to buy item " + itemId + " but there isn't enough in stock");
                }
            }
            throw new IllegalArgumentException("Could not find item id " + itemId);
        }
    }

    public void addItem(Item item, int amount) {
        synchronized (items) {
            saveUpdateState(() -> {
                if (!isOpen()) {
                    LogUtility.error("tried to add item for a closed store");
                    throw new IllegalArgumentException("This store is closed");
                }
        /*if(!isManager(userName) && !isOwner(userName)) {
            LogUtility.error("A user that isn't the store owner/manger tried to add items");
            throw new IllegalArgumentException("This store is closed");
        }*/
                this.items.put(item, items.getOrDefault(item, 0) + amount);
            });
        }
    }

    public void setItemAmount(Item item, int amount) {
        synchronized (items) {
            saveUpdateState(() -> {
                        if (this.items.containsKey(item)) {
                            this.items.put(item, amount);
                        }
                    }
            );
        }
    }

    private User getManagerByName(String name) {
        return this.managers.entrySet().stream().filter(e -> e.getKey().getName().equals(name)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public Permission getPermissionByName(String name) {
        return this.managers.entrySet().stream().filter(e -> e.getKey().getName().equals(name)).findFirst().map(Map.Entry::getValue).orElse(null);
    }

    public void changePermission(String manager, byte permission) {
        if (!isOpen()) {
            LogUtility.error("tried to change permissions for a closed store");
            throw new IllegalArgumentException("This store is closed");
        }
        Permission p = getPermissionByName(manager);
        if (p == null) {
            throw new IllegalArgumentException(String.format("%s is not a manager in the store", manager));
        }
        byte old = p.getPermissionsMask();
        p.setPermissionsMask(permission);
        try {
            PermissionManager.getInstance().addObject(p.toDAL(getManagerByName(manager)));
        } catch (Exception e) {
            p.setPermissionsMask(old);
        }
    }

    public String getFounder() {
        return this.founder;
    }

    public void removeStoreOwner(String removedBy, User owner) {
        synchronized (lock) {
            saveUpdateState(() -> {
                if (!owners.containsKey(owner) || !owners.get(owner).equals(removedBy)) {
                    throw new IllegalArgumentException(String.format("%s is not a store owner that was set by %s", owner, removedBy));
                }
                removeAndRemoveEveryoneAssignedBy(owner);
            });
        }
    }

    public void removeAndRemoveEveryoneAssignedBy(User user) {
        synchronized (lock) {
            owners.remove(user);
            user.removedOwnedStore(getStoreId());
            this.managers.entrySet().removeIf(entry -> entry.getValue().getGivenBy().equals(user.getName()));
            //create new list so no concurrent modification exception
            new ArrayList<>(this.owners.entrySet()).stream().filter(entry -> entry.getValue() != null && entry.getValue().equals(user.getName())).forEach(e -> {
                removeAndRemoveEveryoneAssignedBy(e.getKey());
            });
        }
    }

    public void removeStoreManager(String removedBy, User manager) {
        synchronized (lock) {
            saveUpdateState(() -> {
                        if (!managers.containsKey(manager) || !managers.get(manager).getGivenBy().equals(removedBy)) {
                            throw new IllegalArgumentException(String.format("%s is not a store manager that was set by %s", manager, removedBy));
                        }
                        managers.remove(manager);
                        manager.removedManagedStore(getStoreId());
                        LogUtility.info("Store manager " + manager.getName() + " was removed from position by " + removedBy);
                    }
            );
        }
    }

    public List<String> getManagers() {
        return managers.keySet().stream().map(User::getName).collect(Collectors.toList());
    }

    public List<String> getOwners() {
        return owners.keySet().stream().map(User::getName).collect(Collectors.toList());
    }

    public void changeName(String oldName, String newName) {
        synchronized (lock) {
            saveUpdateState(() -> {
                Set<User> changedOwners = owners.entrySet().stream().filter(e -> e.getValue().equals(oldName)).map(Map.Entry::getKey).collect(Collectors.toSet());
                owners.keySet().removeAll(changedOwners);
                changedOwners.forEach(u -> owners.put(u, newName));

                managers.forEach((key, value) -> {
                    if (value.getGivenBy().equals(oldName)) {
                        value.setGivenBy(newName);
                    }
                });
                LogUtility.info("User named " + oldName + " changed it's name to " + newName);
            });
        }
    }

    public void addDiscount(AbstractDiscountPolicy adp) {
        synchronized (discountLock) {
            saveUpdateState(() -> {
                if (this.discountPolicy == null) {
                    this.discountPolicy = new AddDiscountPolicy();
                    this.discountPolicy.addDiscount(adp);
                    this.discountPolicy.setId(DiscountManager.getInstance().addObject(this.discountPolicy.toDAL()));
                    return;
                }
                AddDiscountPolicy add = new AddDiscountPolicy();
                add.addDiscount(this.discountPolicy);
                add.addDiscount(adp);
                this.discountPolicy = add;
                this.discountPolicy.setId(DiscountManager.getInstance().addObject(this.discountPolicy.toDAL()));
            });
        }
    }

    public void addPolicy(AbstractPurchasePolicy app) {
        synchronized (purchaseLock) {
            saveUpdateState(() -> {
                if (this.purchasePolicy == null) {
                    this.purchasePolicy = new AddPurchasePolicy();
                    this.purchasePolicy.addPolicy(app);
                    this.purchasePolicy.setId(PurchasePolicyManager.getInstance().addObject(this.purchasePolicy.toDAL()));
                    return;
                }
                AddPurchasePolicy ap = new AddPurchasePolicy();
                ap.addPolicy(this.purchasePolicy);
                ap.addPolicy(app);
                this.purchasePolicy = ap;
                this.purchasePolicy.setId(PurchasePolicyManager.getInstance().addObject(this.purchasePolicy.toDAL()));
            });
        }
    }

    public void addExclusiveDiscount(AbstractDiscountPolicy adp) {
        synchronized (discountLock) {
            saveUpdateState(() -> {
                        if (this.discountPolicy == null) {
                            this.discountPolicy = new MaxDiscountPolicy();
                            this.discountPolicy.addDiscount(adp);
                            this.discountPolicy.setId(DiscountManager.getInstance().addObject(this.discountPolicy.toDAL()));
                            return;
                        }
                        MaxDiscountPolicy max = new MaxDiscountPolicy();
                        max.addDiscount(this.discountPolicy);
                        max.addDiscount(adp);
                        this.discountPolicy = max;
                        this.discountPolicy.setId(DiscountManager.getInstance().addObject(this.discountPolicy.toDAL()));
                    }
            );
        }
    }

    public AbstractDiscountPolicy getDiscount(int discountId) {
        synchronized (discountLock) {
            return discountPolicy.getDiscount(discountId);
        }
    }

    public AbstractPurchasePolicy getPolicy(int purchaseId) {
        synchronized (purchaseLock) {
            return purchasePolicy.getPolicies(purchaseId);
        }
    }

    public void removeDiscount(int discountId) {
        synchronized (discountLock) {
            saveUpdateState(() -> {
                boolean res = false;
                if (discountPolicy.getId() == discountId) {
                    this.discountPolicy = null;
                    return;
                }
                res = discountPolicy.removeDiscount(discountId);
                if (!res) {
                    throw new IllegalArgumentException(String.format("There is no discount in store %s with id %s", getStoreId(), discountId));
                }
            });
        }
    }

    public void removePolicy(int purchaseId) {
        synchronized (purchaseLock) {
            saveUpdateState(() -> {

                boolean res = false;
                if (purchasePolicy.getId() == purchaseId) {
                    this.purchasePolicy = null;
                    return;
                }
                res = purchasePolicy.removePolicy(purchaseId);
                if (!res) {
                    throw new IllegalArgumentException(String.format("There is no policy in store %s with id %s", getStoreId(), purchaseId));
                }
            });
        }
    }

    public double applyDiscount(ShoppingBasket sb) {
        synchronized (discountLock) {
            if (this.discountPolicy == null) {
                return sb.calculatePrice();
            }
            return this.discountPolicy.applyDiscount(sb);
        }
    }

    public Map<Item, Double> getDiscounts(ShoppingBasket sb) {
        synchronized (discountLock) {
            if (this.discountPolicy == null) {
                return new HashMap<>();
            }
            return this.discountPolicy.getDiscounts(sb);
        }
    }

    public boolean applyPolicy(ShoppingBasket sb) {
        synchronized (purchaseLock) {
            if (this.purchasePolicy == null) {
                return true;
            }
            return this.purchasePolicy.applyPolicy(sb);
        }
    }

    public boolean canManageDiscounts(User user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canChangeDiscount());
    }
    public boolean canProcessBids(User user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canProcessBids());
    }

    public boolean canManagePurchasePolicy(User user) {
        return isOwner(user) || (isManager(user) && managers.get(user).canChangePurchase());
    }

    public List<SimpleDiscountPolicy> getAllDiscountPolicies() {
        if (this.discountPolicy == null) {
            return new ArrayList<>();
        }
        return this.discountPolicy.getAllDiscountPolicies();
    }

    public List<SimplePurchasePolicy> getAllPurchasePolicies() {
        if (this.purchasePolicy == null) {
            return new ArrayList<>();
        }
        return this.purchasePolicy.getAllPurchasePolicies();
    }

    public void addBid(Bid bid) {
        bids.put(bid.getId(),bid);
    }

    public List<Bid> getBids() {
        return new ArrayList<>(bids.values());
    }
    public Bid getOrThrowBid(int id){
        Bid bid = bids.get(id);
        if (bid == null)
            throw new IllegalArgumentException(String.format("Bid not in store %s", id));
        return bid;
    }
    public boolean isApproved(int bidId){
        List<String> managers = this.managers.keySet().stream().filter(this::canProcessBids).map(User::getName).collect(Collectors.toList());
        List<String> owners = this.owners.keySet().stream().map(User::getName).collect(Collectors.toList());
        return  bids.get(bidId).approvedManagers.containsAll(managers) && bids.get(bidId).approvedManagers.containsAll(owners);
    }

    public Bid removeBid(int bidId){
        Bid bid = bids.get(bidId);
        if (bid == null)
            throw new IllegalArgumentException(String.format("Bid not in store %s", id));
        bids.remove(bidId);
        return bid;
    }

    public StoreDAL toDAL() {
        StoreDAL res = new StoreDAL();
        res.setId(getStoreId());
        res.setFounder(getFounder());
        res.setOpen(open);
        res.setName(getName());
        res.setPermanentlyClosed(permanentlyClosed);
        res.setDiscount(discountPolicy != null ? (CompositeDiscountDAL) discountPolicy.toDAL() : null);
        res.setPurchase(purchasePolicy != null ? (CompositePurchasePolicyDAL) purchasePolicy.toDAL() : null);

        Map<ItemDAL, Integer> items = new HashMap<>();
        getItems().forEach((k, v) -> items.put(k.toDAL(), v));
        res.setItems(items);

        Map<UserDAL, String> owners = new HashMap<>();
        this.owners.forEach((k, v) -> owners.put(k.toDAL(), v));
        res.setOwners(owners);

        Set<PermissionDAL> managers = new HashSet<>();
        this.managers.forEach((k, v) -> managers.add(v.toDAL(k)));
        res.setManagers(managers);
        return res;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void update() {
        if (getStoreId() != 0) {
            StoreManager.getInstance().addObject(toDAL());
        }
    }

    public void saveUpdateState(Runnable r) {
        Store copy = new Store(name, open, id, founder, new HashMap<>(owners), new HashMap<>(managers), new HashMap<>(items), lock, discountLock, purchaseLock, permanentlyClosed, discountPolicy, purchasePolicy, new HashMap<>(bids));
        try {
            r.run();
            update();
        } catch (Exception e) {
            this.name = copy.name;
            this.open = copy.open;
            this.founder = copy.founder;
            this.owners = copy.owners;
            this.managers = copy.managers;
            this.items = copy.items;
            this.permanentlyClosed = copy.permanentlyClosed;
            this.discountPolicy = copy.discountPolicy;
            this.purchasePolicy = copy.purchasePolicy;
            this.bids = copy.bids;
            throw e;
        }
    }
}
