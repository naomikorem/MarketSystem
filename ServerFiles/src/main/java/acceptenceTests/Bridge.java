package acceptenceTests;

import DomainLayer.Response;
import DomainLayer.Stats.Stats;
import DomainLayer.Stores.*;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.SystemManagement.HistoryManagement.History;

import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;

import java.time.LocalDate;
import java.util.*;

public abstract class Bridge {

    // External services tests:
    public abstract Response<Boolean> addExternalPurchaseService(String name, String url);

    public abstract Response<Boolean> addExternalSupplyService(String name, String url);

    public abstract Response<Boolean> removeExternalPurchaseService(String name);

    public abstract Response<Boolean> removeExternalSupplyService(String name);

    public abstract Response<Boolean> purchaseShoppingCart(PaymentParamsDTO paymentParamsDTO, SupplyParamsDTO supplyParamsDTO);

    public abstract Response<Boolean> hasPurchaseService();

    public abstract Response<Boolean> hasSupplyService();

    public abstract Response<Boolean> hasPurchaseService(String purchase_service_name);

    public abstract Response<Boolean> hasSupplyService(String supply_service_name);

    //Acceptance Tests for use case 1 Users:
    public abstract Response<Boolean> enter();

    public abstract Response<Boolean> exit();

    public abstract Response<User> register(String email, String userName, String firstName, String lastName, String password);

    public abstract Response<User> login(String user, String password);

    public abstract Response<Boolean> setUserName(String userName);


    //Acceptance Tests for use case 2:
    public abstract Response<Collection<Store>> getStores();
    
    public abstract Response<Collection<Store>> getUsersStores();
    
    public abstract Response<Store> getStoreInformation(int storeID);

    public abstract Response<Map<Item, Integer>> getItems(int storeId);

    public abstract Response<Permission> getManagersPermissions(int storeId, String managerName);

    public abstract Response<Set<Item>> searchProducts(String productName, String Category, List<String> keywords);

    public abstract Response<Set<Item>> filterResults(Set<Item> items, int upLimit, int lowLimit, int rating);

    public abstract Response<Item> addItemToCart(int storeId, int itemId, int amount);

    public abstract Response<Boolean> removeItemFromCart(int storeId, Item item, int amount);

    public abstract Response<List<Item>> getShoppingCartItems();

    public abstract Response<List<ShoppingBasket>> getCartBaskets();

    public abstract Response<List<Item>> updateItemInCart(int storeId, int itemId, int amount);

    //Acceptance Tests for use case 3:
    public abstract Response<Boolean> logout();

    public abstract Response<Store> addNewStore(String storeName);

    //Acceptance Tests for use case 4:
    public abstract Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount);

    public abstract Response<Boolean> updateStorePolicy(int storeId);

    public abstract Response<Boolean> updateDiscountPolicy(int storeId);

    public abstract Response<Boolean> addOwner(String owner, int storeId);

    public abstract Response<Boolean> addManager(String managerName, int storeId);

    public abstract Response<Boolean> updateManagerPermissions(int storeId, String managerName, Byte newPermission);

    public abstract Response<List<String>> getStoreManagers(int store);

    public abstract Response<Boolean> removeOwner(String toRemove, int storeId);

    public abstract Response<Boolean> closeStore(int storeId);

    public abstract Response<Item> removeItemFromStore(int storeId, int itemId, int amount);

    // History:
    public abstract Response<History> getPurchaseHistory();

    public abstract Response<History> getPurchaseHistory(String username);

    public abstract Response<History> getStoreHistory(int store_id);

    // Notifications:
    public abstract Response<List<INotification>> getUserNotifications();

    // Use case 6
    public abstract Response<User> getUser(String userName);

    public abstract Response<Boolean> isLoggedInAdminCheck();

    public abstract Response<Boolean> removeManager(String toRemove, int storeId);

    public abstract Response<Boolean> permanentlyCloseStore(int storeId);

    public abstract Response<List<String>> getStoreOwners(int store_id);

    public abstract Response<Boolean> hasAdmin();

    public abstract Response<Boolean> addAdmin(String name);

    public abstract Response<Boolean> deleteAdmin(String name);

    public abstract Response<SimpleDiscountPolicy> addDiscount(int storeId, double percentage);

    public abstract Response<Boolean> addItemPredicateToDiscount(int storeId, int discountId, String type, int itemId);

    public abstract Response<Boolean> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName);

    public abstract Response<Double> getCartPrice();

    public abstract Response<Boolean> getIsLegalToPurchase(int storeId);

    public abstract Response<Boolean> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice);

    public abstract Response<SimpleDiscountPolicy> addExclusiveDiscount(int storeId, double percentage);

    public abstract Response<Boolean> removeDiscount(int storeId, int discountId);

    public abstract Response<SimplePurchasePolicy> addPolicy(int storeId, int hour, Calendar date);

    public abstract Response<Boolean> removePolicy(int storeId, int policyId);


    public abstract Response<AbstractPurchasePolicy> addItemPredicateToPolicy(int storeId, int policyId, String type, int itemId, int hour);


    public abstract Response<AbstractPurchasePolicy> addItemNotAllowedInDatePredicateToPolicy(int storeId, int policyId, String type, int itemId, Calendar date);

    public abstract Response<Boolean> setItemRating(int storeId, int itemId, double rate);

    public abstract Response<Double> getItemRating(int storeId, int itemId);

    public abstract Response<Boolean> addBid(int storeId, double bidPrice, int item, int amount);

    public abstract Response<Boolean> addBidToCart(int bidId);

    public abstract Response<Collection<Bid>> getBids(int storeId);

    public abstract Response<Collection<Bid>> getUserBids();

    public abstract Response<Bid> approveBid(int storeId, int bidId);

    public abstract Response<Bid> deleteBid( int storeId, int bidId) ;

    public abstract Response<Bid> updateBid(int storeId,int bidId, double newPrice);
/*
    public abstract Response<Boolean> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName);

    public abstract Response<Double> getCartPrice();

    public abstract Response<Boolean> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice);

    public abstract Response<AbstractDiscountPolicy> addExclusiveDiscount(int storeId, double percentage);


    */

    public abstract Response<List<Map.Entry<LocalDate, Stats>>> getStats();

    public abstract Response<Boolean> setManagerPermission(String manager, int storeId, byte permission);

    public abstract Response<AbstractPurchasePolicy> addItemLimitPredicateToPolicy(int storeId, int policyId, String type, int itemId, int min, int max);
    }
