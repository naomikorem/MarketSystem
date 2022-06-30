package DomainLayer;

import DomainLayer.Stats.Stats;
import DomainLayer.Stores.*;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.SupplyParamsDTO;
import ServiceLayer.DTOs.PaymentParamsDTO;

import java.time.LocalDate;
import java.util.*;

public interface SystemInterface {
    public Response<Boolean> removeItemIDFromCart(int storeId, int itemid, int amount);
    public Response<Item> removeItemFromStore(int storeId, int itemId, int amount);

    public Response<Item> modifyItem(int storeId, int itemId, String productName, String category, double price, int amount, List<String> keywords);

    public Response<Map<Item, Integer>> getItems(int storeId);


    public Response<List<String>> getStoreOwners(int storeId);

    public Response<Boolean> setItemRating(int storeId, int itemId, double rate) ;

    public Response<Double> getItemRating(int storeId, int itemId);

    public Response<Boolean> addExternalPurchaseService(String name, String url);

    public Response<Boolean> addExternalSupplyService(String name, String url);

    public Response<Boolean> removeExternalPurchaseService(String name);

    public Response<Boolean> removeExternalSupplyService(String name);


    public Response<Boolean> purchaseShoppingCart(PaymentParamsDTO paymentParamsDTO, SupplyParamsDTO supplyParamsDTO);

    public Response<Boolean> hasPurchaseService();

    public Response<Boolean> hasSupplyService();

    public Response<Boolean> hasPurchaseService(String purchase_service_name);

    public Response<Boolean> hasSupplyService(String purchase_supply_name);

    public Response<List<String>> getAllExternalSupplyServicesNames();

    public Response<List<String>> getAllExternalPurchaseServicesNames();

    public Response<History> getPurchaseHistory();
    public Response<History> getPurchaseHistory(String username);
    public Response<User> getUser(String userName);

    public Response<History> getStoreHistory(int store_id);

    public Response<Boolean> enter();

    public Response<Boolean> exit();

    public Response<User> register(String email, String userName, String firstName, String lastName, String password);

    public Response<User> login(String user, String password);

    public Response<Boolean> logout();

    public Response<List<Item>> getShoppingCartItems();

    public Response<Boolean> addManager(String manager, int storeId);

    Response<Boolean> addOwner(String owner, int storeId);

    Response<Boolean> setManagerPermission(String manager, int storeId, byte permission);

    public Response<Collection<Store>> getAllStores();
    public Response<Collection<Store>> getAllOpenStores();
    public Response<Collection<Store>> getStoresBesidesPermanentlyClosed();

    public Response<Store> getStore(int id);

    Response<List<ShoppingBasket>> getCartBaskets();

    public Response<Item> addItemToCart(int storeId, int itemId, int amount);

    public Response<Boolean> closeStore(int storeId);
    public Response<Boolean> reopenStore(int storeId);

    public Response<Boolean> permanentlyCloseStore(int storeId);

    Response<Boolean> removeOwner(String toRemove, int storeId);

    Response<Boolean> removeManager(String toRemove, int storeId);

    public Response<Store> addNewStore(String name);

    public Response<Item> addItemToStore(int storeId, String name, Category category, double price, int amount);

    public Response<Item> addItemToStore(int storeId, String name, String category, double price, int amount);

    public Response<Boolean> deleteUser(String name);
    public Response<List<String>> getStoreManagers(int storeId);

    public Response<Permission> getManagersPermissions(int storeId, String managerName);

    public Response<Boolean> setUserName(String newUserName);

    public Response<List<INotification>> getUserNotifications();

    public Response<Boolean> removeUserNotifications();

    public Response<Set<Item>> searchProducts(String productName, String category, List<String> keywords);

    public Response<Set<Item>> filterProdacts(Set<Item> items, int upLimit, int lowLimit, int rating);

    public Response<Boolean> deleteAdmin(String name);
    public Response<Boolean> addAdmin(String name);

    public Response<Boolean> hasAdmin();


    public Response<Boolean> removeItemFromCart(int storeId, Item item, int amount);

    public Response<SimpleDiscountPolicy> addDiscount(int storeId, double percentage);

    public Response<SimplePurchasePolicy> addPolicy(int storeId, int hour, Calendar date);

    public Response<AbstractDiscountPolicy> addItemPredicateToDiscount(int storeId, int discountId, String type, int itemId);

    public Response<AbstractPurchasePolicy> addItemPredicateToPolicy(int storeId, int policyId, String type, int itemId, int hour) ;

    public Response<AbstractPurchasePolicy> addItemNotAllowedInDatePredicateToPolicy(int storeId, int policyId, String type, int itemId, Calendar date) ;

    public Response<AbstractDiscountPolicy> addCategoryPredicateToDiscount(int storeId, int discountId, String type, String categoryName);

    public Response<Double> getCartPrice();

    public Response<Boolean> getIsLegalToPurchase(int storeId);

    public Response<AbstractDiscountPolicy> addBasketRequirementPredicateToDiscount(int storeId, int discountId, String type, double minPrice);

    public Response<SimpleDiscountPolicy> addExclusiveDiscount(int storeId, double percentage);

    public Response<Boolean> removeDiscount(int storeId, int discountId);

    public Response<Boolean> removePolicy(int storeId, int policyId);

    public Response<Collection<Store>> getUsersStores();
    public Response<String[]> getStoreNameByID(int id) ;
    public Response<Double> calculateShoppingCartPriceResult(List<ShoppingBasket> baskets);
    public Response<Map<Item, Double>> getShoppingBasketDiscounts(ShoppingBasket sb);
    public Response<Boolean> addBid(int storeId, double bidPrice, int item, int amount);
    public Response<Collection<Bid>> getBids(int storeId) ;
    public Response<Collection<Bid>> getUserBids();
    public Response<Bid> approveBid(int storeId, int bidId);
    public Response<Bid> updateBid(int storeId,int bidId, double newPrice);
    public Response<Bid> deleteBid( int storeId, int bidId);
    public Response<Boolean> addBidToCart(int bidId);
    public Response<Boolean> approveAllBids(int storeId);

    public Response<Boolean> isLoggedInAdminCheck();

    Response<List<Map.Entry<LocalDate, Stats>>> getStats();
    public Response<Boolean> addOwnerAgreement(String owner, int storeId);
    public Response<OwnerAgreement> approveOwnerAgreement(int storeId, String bidId);


    }
