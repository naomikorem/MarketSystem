package DomainLayer.Stores;

import DataLayer.DALObjects.PredicateDAL;
import DataLayer.DALObjects.SimplePredicateDAL;
import DataLayer.PredicateManager;
import DomainLayer.Response;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.Predicates.AndCompositePredicate;
import DomainLayer.Stores.Predicates.CompositePredicate;
import DomainLayer.Stores.Predicates.CompositePredicate.PredicateEnum;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import Exceptions.LogException;
import Utility.LogUtility;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StoreController {

    private PredicateManager manager;
    private Map<Integer, Store> stores; // store-id , stores

    private AtomicInteger NEXT_STORE_ID = new AtomicInteger(1);
    private AtomicInteger NEXT_DISCOUNT_ID = new AtomicInteger(1);
    private AtomicInteger NEXT_POLICY_ID = new AtomicInteger(1);
    private AtomicInteger NEXT_BID_ID = new AtomicInteger(1);

    public StoreController() {
        this.stores = new HashMap<>();
        this.manager = PredicateManager.getInstance();
    }

    private static class StoreControllerHolder {
        static final StoreController instance = new StoreController();
    }

    public static StoreController getInstance() {
        return StoreControllerHolder.instance;
    }

    public void clearAll() {
        stores = new HashMap<>();
        Item.NEXT_ITEM_ID = 1;
    }

    private synchronized int getNewStoreId() {
        return NEXT_STORE_ID.getAndIncrement();
    }

    private synchronized int getNewDiscountId() {
        return NEXT_DISCOUNT_ID.getAndIncrement();
    }

    private synchronized int getNewPolicyId() {
        return NEXT_POLICY_ID.getAndIncrement();
    }
    private synchronized int getNewBidId() {
        return NEXT_POLICY_ID.getAndIncrement();
    }

    public Store createStore(User owner, String store_name) {
        Store store = new Store(owner, store_name, getNewStoreId());
        addStore(store);
        return store;
    }

    public Boolean isExist(int storeId) { return this.stores.getOrDefault(storeId, null)!=null;}
    private void addStore(Store store) {
        this.stores.put(store.getStoreId(), store);
    }
    /**for debugging**/
    public void removeStore(Store store) {
        this.stores.remove(store.getStoreId());
    }

    public Store getStore(int id) {
        return stores.getOrDefault(id, null);
    }

    public Store getStoreAndThrow(int storeId) {
        Store s = getStore(storeId);
        if (s == null) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        return s;
    }

    public Collection<Store> getAllStores() {
        return stores.values();
    }

    public Collection<Store> getAllOpenStores() {
        return stores.values().stream().filter(Store::isOpen).collect(Collectors.toList());
    }

    public Collection<Store> getStoresBesidesPermanentlyClosed() {
        return stores.values().stream().filter(store -> !store.isPermanentlyClosed()).collect(Collectors.toList());
    }

    public Set<Item> getItemsWithNameContains(String name) {
        return stores.values().stream().map(s -> s.getItemsWithNameContains(name)).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithKeyWord(List<String> kws) {
        return stores.values().stream().map(s -> s.getItemsWithKeyWords(kws)).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Set<Item> getItemsWithCategory(String categoryString) throws IllegalArgumentException {
        try {
            Category category = Category.valueOf(categoryString);
            return stores.values().stream().map(s -> s.getItemsWithCategory(category)).flatMap(Set::stream).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new IllegalArgumentException("Category dose not exist");
        }
    }


    public Item addItemToStore(User manager, int storeId, String name, Category category, double price, int amount) {
        if (!manager.isSubscribed()) {
            throw new IllegalArgumentException("User has to be logged in to do this action.");
        }
        Store s = getStoreAndThrow(storeId);
        if (!s.canManageItems(manager)) {
            throw new LogException("You cannot add items to this store", String.format("User %s tried to add an item to a store that they do not manager", manager));
        }
        Item i = new Item(name, category, price);
        s.addItem(i, amount);
        LogUtility.info(String.format("%s added %s %s to store id %s", manager.getName(), amount, name, storeId));
        return i;
    }

    public Item reserveItemFromStore(int storeId, int itemId, int amount) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemAndDeduct(itemId, amount);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        LogUtility.info(String.format("%s %s were reserved in store %s", amount, i.getProductName(), storeId));
        return i;
    }

    public Item removeItemFromStore(User owner, int storeId, int itemId, int amount) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageItems(owner)) {
            throw new LogException("You cannot remove items from this store.", String.format("a user tried to illegally edit store %s", storeId));
        }
        Item i = s.getItemAndDeduct(itemId, amount);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        LogUtility.info(String.format("%s %s were taken out of store %s by %s", amount, i.getProductName(), storeId, owner.getName()));
        return i;
    }

    public Double getRatingOfItem(int storeId, int itemId) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        return i.getRate();
    }

    public Boolean setRatingOfItem(int storeId, int itemId, double rate) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        try {
            i.updateRate(rate);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public Item getItemFromStore(int storeId, int itemId) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with item id %s in the store", itemId));
        }
        return i;
    }

    public boolean addManager(User owner, User manager, int storeId) {
        if (owner == null || manager == null) {
            throw new IllegalArgumentException("A user cannot be null.");
        }
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canAssignManager(owner)) {
            throw new IllegalArgumentException("This user cannot assign a manager");
        }
        if (!s.canBecomeManager(manager)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a manager of the store with store id %s", manager.getName(), storeId));
        }
        s.addManager(owner.getName(), manager);
        LogUtility.info(String.format("%s assigned %s as a manager at store %s", owner.getName(), manager, storeId));
        return true;
    }

    public boolean addOwner(User owner, User newOwner, int storeId) {
        if (owner == null || newOwner == null) {
            throw new IllegalArgumentException("A user cannot be null.");
        }
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canAssignOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign another user to be an owner");
        }
        if (!s.canBecomeOwner(newOwner)) {
            throw new IllegalArgumentException(String.format("%s cannot be promoted to be a an owner of the store with store id %s", newOwner.getName(), storeId));
        }
        s.addOwner(owner.getName(), newOwner);
        LogUtility.info(String.format("%s assigned %s as an owner at store %s", owner.getName(), newOwner, storeId));
        return true;
    }

    public Item returnItemToStore(int storeId, Item item, int amount) {
        Store s = getStoreAndThrow(storeId);
        s.addItem(item, amount);
        LogUtility.info(String.format("%s %s were returned to store %s", amount, item.getProductName(), storeId));
        return item;
    }

    public boolean isShopOwner(Store store, String shopOwnerName) {
        return stores.get(store.getStoreId()).isOwner(shopOwnerName);
    }

    public void setManagerPermission(User owner, String manager, int storeId, byte permission) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot assign manager's permission");
        }
        s.changePermission(manager, permission);
        LogUtility.info(String.format("%s changed the permissions of manager %s", owner.getName(), manager));
    }

    public Store closeStore(User user, int storeId) {
        if (!isExist(storeId)) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!user.isSubscribed()) {
            throw new IllegalArgumentException("Only logged in users can perform this action.");
        }
        stores.get(storeId).setIsOpen(user.getName(), false);
        LogUtility.info(String.format("User %s just closed store %s", user.getName(), storeId));
        return stores.get(storeId);
    }

    public Store reopenStore(User user, int storeId) {
        if (!isExist(storeId)) {
            throw new IllegalArgumentException(String.format("There is no store with id %s", storeId));
        }
        if (!user.isSubscribed()) {
            throw new IllegalArgumentException("Only logged in users can perform this action.");
        }
        stores.get(storeId).setIsOpen(user.getName(), true);
        LogUtility.info(String.format("User %s just reopened store %s", user.getName(), storeId));
        return stores.get(storeId);
    }

    public Store permanentlyCloseStore(int storeId) {
        Store s = getStoreAndThrow(storeId);
        s.setPermanentlyClosed(true);
        LogUtility.info(String.format("store %s was permanently closed by an admin", storeId));
        return s;
    }

    public void removeOwner(User owner, User toRemove, int storeId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed()) {
            throw new IllegalArgumentException("Guest users can not perform this action.");
        }
        s.removeStoreOwner(owner.getName(), toRemove);
        LogUtility.info(String.format("%s removed %s from being a store owner at store %s", owner.getName(), toRemove.getName(), storeId));
    }

    public void removeManager(User owner, User toRemove, int storeId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed()) {
            throw new IllegalArgumentException("Guest users can not perform this action.");
        }
        s.removeStoreManager(owner.getName(), toRemove);
        LogUtility.info(String.format("%s removed %s from being a store manager at store %s", owner.getName(), toRemove.getName(), storeId));
    }

    public void removeUserRoles(User owner, User removed) {
        for (int id : removed.getManagedStores()) {
            Store s = getStore(id);
            s.removeStoreManager(owner.getName(), removed);
        }
        for (int id : removed.getOwnedStores()) {
            Store s = getStore(id);
            s.removeStoreOwner(owner.getName(), removed);
        }
    }

    public Item modifyItem(User owner, int storeId, int itemId, String productName, String category, double price, int amount, List<String> keywords) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with id %s in store %s", itemId, storeId));
        }
        if (!owner.isSubscribed() || !s.canManageItems(owner)) {
            throw new IllegalArgumentException("Only store owners can perform this action.");
        }
        i.updateItem(productName, Category.valueOf(category), price, keywords);
        s.setItemAmount(i, amount);
        return i;
    }

    public Item setItemAmount(User owner, int storeId, int itemId, int amount) {
        Store s = getStoreAndThrow(storeId);
        Item i = s.getItemById(itemId);
        if (i == null) {
            throw new IllegalArgumentException(String.format("There is no item with id %s in store %s", itemId, storeId));
        }
        if (!owner.isSubscribed() || !s.canManageItems(owner)) {
            throw new IllegalArgumentException("Only store owners can perform this action.");
        }
        s.setItemAmount(i, amount);
        return i;
    }


    public Map<Item, Integer> getItems(int storeId) {
        Store s = getStoreAndThrow(storeId);
        return s.getItems();
    }

    public Permission getManagersPermissions(User owner, int storeId, String managerName){
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        Permission result = s.getPermissionByName(managerName);
        return result;
    }

    public List<String> getManagers(User owner, int storeId){
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        List<String> result = s.getManagers();
        LogUtility.info(String.format("%s got list of managers: %s from store %s", owner.getName(), result, storeId));
        return result;
    }

    public Set<Item> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating){
        Set<Item> output = new HashSet<>();
        for (Item item: items) {
            if(upLimit == -1 || item.getPrice() <= upLimit)
                if(lowLimit == -1 || item.getPrice() >= lowLimit)
                    if(rating== -1 || item.getRate() > rating)
                        output.add(item);
        }
        return output;
    }

    public void applyChangeName(User u, String oldName, String newName) {
        for (int id : u.getOwnedStores()) {
            Store s = getStore(id);
            if (s != null) {
                s.changeName(oldName, newName);
            }
        }
        for (int id : u.getManagedStores()) {
            Store s = getStore(id);
            if (s != null) {
                s.changeName(oldName, newName);
            }
        }
    }

    private SimpleDiscountPolicy createNewDiscount(User owner, Store s, double percentage) {
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimpleDiscountPolicy sdp = new SimpleDiscountPolicy(percentage, null);
        sdp.setId(getNewDiscountId());
        return sdp;
    }

    private SimplePurchasePolicy createNewPolicy(User owner, Store s, int hour, Calendar date) {
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePurchasePolicy spp = new SimplePurchasePolicy(null);
        spp.setId(getNewPolicyId());
        if (hour!=24)
            spp.setHour(hour);
        return spp;
    }

    public SimpleDiscountPolicy addDiscount(User owner, int storeId, double percentage) {
        Store s = getStoreAndThrow(storeId);
        SimpleDiscountPolicy sdp = createNewDiscount(owner, s, percentage);
        s.addDiscount(sdp);
        return sdp;
    }

    public SimplePurchasePolicy addPolicy(User owner, int storeId, int hour, Calendar date) {
        Store s = getStoreAndThrow(storeId);
        SimplePurchasePolicy spp = createNewPolicy(owner, s, hour, date);
        s.addPolicy(spp);
        return spp;
    }

    public SimpleDiscountPolicy addExclusiveDiscount(User owner, int storeId, double percentage) {
        Store s = getStoreAndThrow(storeId);
        SimpleDiscountPolicy sdp = createNewDiscount(owner, s, percentage);
        s.addExclusiveDiscount(sdp);
        return sdp;
    }

    public void removeDiscount(User owner, int storeId, int discountId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        s.removeDiscount(discountId);
    }

    public void removePolicy(User owner, int storeId, int policyId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        s.removePolicy(policyId);
    }
  
    public AbstractDiscountPolicy addPredicateToDiscount(User owner, Store s, int discountId, PredicateEnum type, SimplePredicate sp) {
        AbstractDiscountPolicy adp = s.getDiscount(discountId);
        if (adp == null) {
            throw new LogException(String.format("There is no discount with id %s", discountId), String.format("user %s tried to add a predicate to non existing discount with id %s", owner.getName(), discountId));
        }
        switch (type) {
            case AND:
                adp.addAndPredicate(sp);
                break;
            case OR:
                adp.addOrPredicate(sp);
                break;
            case XOR:
                adp.addXorPredicate(sp);
                break;
        }

        Integer id = this.manager.addObject(sp.toDAL());
        sp.setId(id);

        return adp;
    }

    public List<SimpleDiscountPolicy> getAllDiscountPolicies(User owner, int storeId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        return s.getAllDiscountPolicies();
    }

    public List<SimplePurchasePolicy> getAllPurchasePolicies(User owner, int storeId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot see the policies");
        }
        return s.getAllPurchasePolicies();
    }

    public AbstractPurchasePolicy addPredicateToPolicy(User owner, Store s, int policyId, PredicateEnum type, SimplePredicate sp) {
        AbstractPurchasePolicy app = s.getPolicy(policyId);
        if (app == null) {
            throw new LogException(String.format("There is no discount with id %s", policyId), String.format("user %s tried to add a predicate to non existing discount with id %s", owner.getName(), policyId));
        }
        switch (type) {
            case AND:
                app.addAndPredicate(sp);
                break;
            case OR:
                app.addOrPredicate(sp);
                break;
            case COND:
                app.addCondPredicate(sp);
                break;
        }
        return app;
    }

    public AbstractDiscountPolicy addItemPredicateToDiscount(User owner, int storeId, int discountId, String type, int itemId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePredicate sp = new SimplePredicate(itemId);
        return addPredicateToDiscount(owner, s, discountId, PredicateEnum.valueOf(type), sp);
    }

    public void changeDiscountPercentage(User owner, int storeId, int discountId, double newPercentage) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        AbstractDiscountPolicy adp = s.getDiscount(discountId);
        adp.setPercentage(newPercentage);
    }

    public void changePolicyHour(User owner, int storeId, int policyId, int newHour, Calendar newDate) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        AbstractPurchasePolicy adp = s.getPolicy(policyId);
        adp.setHour(newHour);
        adp.setDate(newDate);

    }

    public AbstractPurchasePolicy addItemPredicateToPolicy(User owner, int storeId, int policyId, String type, int itemId, int hour) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot add items-predicate to policies");
        }
        SimplePredicate sp = new SimplePredicate(itemId,hour);
        return addPredicateToPolicy(owner, s, policyId, PredicateEnum.valueOf(type), sp);
    }

    public AbstractPurchasePolicy addItemNotAllowedInDatePredicateToPolicy(User owner, int storeId, int policyId, String type, int itemId, Calendar date) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot add items-predicate to policies");
        }
        SimplePredicate sp = new SimplePredicate(itemId,date);
        return addPredicateToPolicy(owner, s, policyId, PredicateEnum.valueOf(type), sp);
    }

    public AbstractDiscountPolicy addCategoryPredicateToDiscount(User owner, int storeId, int discountId, String type, String categoryName) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePredicate sp = new SimplePredicate(Category.valueOf(categoryName));
        return addPredicateToDiscount(owner, s, discountId, PredicateEnum.valueOf(type), sp);
    }

    public AbstractDiscountPolicy addBasketRequirementPredicateToDiscount(User owner, int storeId, int discountId, String type, double minPrice) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManageDiscounts(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePredicate sp = new SimplePredicate(0, minPrice, null, 0, null, SimplePredicate.PredicateType.Basket);
        sp.setDisplayString(String.format("Basket has to cost at least %s", minPrice));
        return addPredicateToDiscount(owner, s, discountId, PredicateEnum.valueOf(type), sp);
    }

    public void addHourForItemBasketRequirementPredicateToPolicy(User owner, int storeId, int policyId, String type, int hour, int itemId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePredicate sp = new SimplePredicate(itemId, hour);
        addPredicateToPolicy(owner, s, policyId, PredicateEnum.valueOf(type), sp);
    }

    public void addBasketRequirementPredicateToPolicy(User owner, int storeId, int policyId, String type, double minPrice) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.canManagePurchasePolicy(owner)) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        SimplePredicate sp = new SimplePredicate(0, minPrice, null, 0, null, SimplePredicate.PredicateType.Basket);
        addPredicateToPolicy(owner, s, policyId, PredicateEnum.valueOf(type), sp);
    }
/*
    public void addAgeBasketRequirementPredicateToPolicy(User owner, int storeId, int policyId, String type, int age, int itemId) {
        Store s = getStoreAndThrow(storeId);
        if (!owner.isSubscribed() || !s.isOwner(owner.getName())) {
            throw new IllegalArgumentException("This user cannot see the managers");
        }
        if(s.)
        SimplePredicate sp = new SimplePredicate(itemId, age);
        addPredicateToPolicy(owner, s, policyId, PredicateEnum.valueOf(type), sp);
    }*/

    public double getShoppingBasketPrice(ShoppingBasket sb) {
        Store s = getStoreAndThrow(sb.getStoreId());
        return s.applyDiscount(sb);
    }
    public Map<Item, Double> getShoppingBasketDiscounts(ShoppingBasket sb) {
        Store s = getStoreAndThrow(sb.getStoreId());
        return s.getDiscounts(sb);
    }

    public boolean getShoppingBasketPolicy(ShoppingBasket sb) {
        Store s = getStoreAndThrow(sb.getStoreId());
        return s.applyPolicy(sb);
    }
    public String[] getStoreNameByID(int id){
        String[] s = new String[1];
        s[0] = getStoreAndThrow(id).getName();
        return s;
    }
    public Bid addBid(int storeId,String costumerName, double bidPrice, int item, int amount) {
        Store s = getStoreAndThrow(storeId);
        Bid bid = new Bid(getNewBidId(), costumerName, bidPrice, item, amount);
        s.addBid(bid);
        return bid;
    }
    public Bid approveBid(int storeId, User user, int bidId){
        Store s = getStoreAndThrow(storeId);
        if (!s.canProcessBids(user))
            throw new IllegalArgumentException(String.format("Have to process bids permission in store %s", storeId));
        Bid bid = s.getOrThrowBid(bidId);
        bid.approve(user.getName());
        if(s.isApproved(bidId)){
            bid.setApproved(true);
        }
        return bid;
    }
    public Bid removeBid(int storeId, User user, int bidId){
        Store s = getStoreAndThrow(storeId);
        Bid bid = s.getOrThrowBid(bidId);
        if(!bid.getCostumer().equals(user.getName())){
            if (!s.canProcessBids(user))
                throw new IllegalArgumentException(String.format("Have to process bids permission in store %s", storeId));
        }
        return s.removeBid(bidId);
    }

    public Collection<Bid> getBids(int storeId) {
        Store s = getStoreAndThrow(storeId);
        return s.getBids();
    }
    public List<Bid> getBids( int storeId, String userName) {
        Store s = getStoreAndThrow(storeId);
        return s.getBids(userName);
    }
}
