package ServiceLayer;

import DomainLayer.Response;
import DomainLayer.Stats.Stats;
import DomainLayer.Stats.StatsController;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;
import DomainLayer.Stores.DiscountPolicy.SimpleDiscountPolicy;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;
import DomainLayer.Stores.*;
import DomainLayer.Stores.PurchasePolicy.SimplePurchasePolicy;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Service {

    public static final String SYSTEM_IMPLEMENTOR_STRING = "SystemImplementor";
    public static final String IP_STRING = "ip";
    @Autowired
    private SimpMessagingTemplate template;

    public Service() {
        super();
        //MarketManagementFacade.getInstance().initializeMarket();
    }

    @MessageMapping("/market/login")
    @SendToUser("/topic/loginResult")
    public Response<UserDTO> login(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login(map.get("user"), map.get("pass"));
        if (user.hadError()) {
            return new Response<>(user.getErrorMessage());
        }
        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/getToken")
    @SendToUser("/topic/tokenResult")
    public Response<String> getToken(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getToken();
    }

    @MessageMapping("/market/getRating")
    @SendToUser("/topic/ratingResult")
    public Response<Double> getRating(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getItemRating(map.get("storeId"), map.get("itemId"));
    }


    @MessageMapping("/market/setRating")
    @SendToUser("/topic/setRatingResult")
    public Response<Boolean> setRating(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).setItemRating((Integer) map.get("storeId"), (Integer)map.get("itemId"),(Double)map.get("rating"));
    }

    @MessageMapping("/market/loginByToken")
    @SendToUser("/topic/loginByTokenResult")
    public Response<UserDTO> loginByToken(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).loginUserByToken(map.get("token"));
        if (user.hadError()) {
            return new Response<>(user.getErrorMessage());
        }
        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/register")
    @SendToUser("/topic/registerResult")
    public Response<UserDTO> register(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).
                register(map.get("email"), map.get("username"), map.get("firstname"), map.get("lastname"), map.get("pass"));
        if (user.hadError())
            return new Response<>(user.getErrorMessage());
        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/getStores")
    @SendToUser("/topic/getStoresResult")
    public Response<List<StoreDTO>> getStores (SimpMessageHeaderAccessor headerAccessor) {

        Response<Collection<Store>> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllStores();
        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        List<StoreDTO> dto_stores = stores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }

    @MessageMapping("/market/getOpenStores")
    @SendToUser("/topic/getOpenStoresResult")
    public Response<List<StoreDTO>> getOpenStores (SimpMessageHeaderAccessor headerAccessor) {
        Response<Collection<Store>> openStores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllOpenStores();

        if (openStores.hadError())
            return new Response<>(openStores.getErrorMessage());
        List<StoreDTO> dto_stores = openStores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }

    @MessageMapping("/market/getStoresBesidesPermanentlyClosed")
    @SendToUser("/topic/getStoresBesidesPermanentlyClosedResult")
    public Response<List<StoreDTO>> getStoresBesidesPermanentlyClosed(SimpMessageHeaderAccessor headerAccessor) {
        Response<Collection<Store>> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStoresBesidesPermanentlyClosed();

        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        List<StoreDTO> dto_stores = stores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }


    @MessageMapping("/market/AddItemToCart")
    @SendToUser("/topic/AddItemToCartResult")
    public Response<ItemDTO> AddItemToCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<Item> item = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING))
                .addItemToCart(map.get("store_id"), map.get("item_id"), map.get("amount"));
        if (item.hadError())
            return new Response<>(item.getErrorMessage());

        return new Response<>(convertToItemDTO(item.getObject(), map.get("amount")));
    }


    @MessageMapping("/market/removeItemToCart")
    @SendToUser("/topic/removeItemToCartResult")
    public Response<Boolean> removeItemToCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<Boolean> item = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING))
                .removeItemIDFromCart(map.get("store_id"), map.get("item_id"), map.get("amount"));
        if (item.hadError())
            return new Response<>(item.getErrorMessage());
        return new Response<>(item.getObject());
    }

    @MessageMapping("/market/getUsersStores")
    @SendToUser("/topic/getUsersStoresResult")
    public Response<List<StoreDTO>> getUsersStores(SimpMessageHeaderAccessor headerAccessor) {
        Response<Collection<Store>> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getUsersStores();
        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        List<StoreDTO> dto_stores = stores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());
        return new Response<>(dto_stores);
    }

    @MessageMapping("/market/closeStorePermanently")
    @SendToUser("/topic/closeStorePermanentlyResult")
    public Response<Boolean> closeStorePermanentlyStore(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).permanentlyCloseStore(map.get("storeId"));
    }

    @MessageMapping("/market/closeStore")
    @SendToUser("/topic/closeStoreResult")
    public Response<Boolean> closeStore(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).closeStore(map.get("storeId"));
    }

    @MessageMapping("/market/reopenStore")
    @SendToUser("/topic/reopenStoreResult")
    public Response<Boolean> reopenStoreStore(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).reopenStore(map.get("storeId"));
    }

    @MessageMapping("/market/removeSubscription")
    @SendToUser("/topic/removeSubscriptionResult")
    public Response<Boolean> removeUserSubscription(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).deleteUser(map.get("username"));
    }

    @MessageMapping("/market/getPersonalHistory")
    @SendToUser("/topic/getPersonalHistoryResult")
    public Response<HistoryDTO> getPersonalPurchaseHistory(SimpMessageHeaderAccessor headerAccessor) {
        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).purchaseShoppingCart("ashdod", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);

        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getPurchaseHistory();
        if (history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
    }

    @MessageMapping("/market/getUserHistory")
    @SendToUser("/topic/getUserHistoryResult")
    public Response<HistoryDTO> getUserPurchaseHistory(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getPurchaseHistory(map.get("username"));
        if (history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
    }

    @MessageMapping("/market/getStoreHistory")
    @SendToUser("/topic/getStoreHistoryResult")
    public Response<HistoryDTO> getStorePurchaseHistory(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStoreHistory(map.get("storeId"));
        if (history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
    }


    @MessageMapping("/market/getSubscriberInfo")
    @SendToUser("/topic/getSubscriberInfoResult")
    public Response<UserDTO> getSubscriberInfo(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getUser(map.get("username"));
        if(user.hadError())
            return new Response<>(user.getErrorMessage());

        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/SetItemRate")
    @SendToUser("/topic/SetItemRateResult")
    public Response<Boolean> SetItemRate(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Integer store_id = (Integer) map.get("store_id");
        Integer item_id = (Integer) map.get("item_id");
        Double rate = Double.parseDouble((String) map.get("rate"));

        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).setItemRating(store_id, item_id, rate);
        return res;
    }

    @MessageMapping("/market/addAdmin")
    @SendToUser("/topic/addAdminResult")
    public Response<Boolean> addAdmin(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return  ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addAdmin((String) map.get("name"));
    }

    @MessageMapping("/market/addExternalPurchaseService")
    @SendToUser("/topic/addExternalServiceResult")
    public Response<Boolean> addExternalPurchaseService(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addExternalPurchaseService(map.get("name"), map.get("address"));
    }

    @MessageMapping("/market/addExternalSupplyService")
    @SendToUser("/topic/addExternalServiceResult")
    public Response<Boolean> addExternalSupplyService(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addExternalSupplyService(map.get("name"), map.get("address"));
    }

    @MessageMapping("/market/getAllExternalSupplyServicesNames")
    @SendToUser("/topic/getAllExternalSupplyServicesNamesResult")
    public Response<List<String>> getAllExternalSupplyServicesNames(SimpMessageHeaderAccessor headerAccessor) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllExternalSupplyServicesNames();
    }

    @MessageMapping("/market/getAllExternalPurchaseServicesNames")
    @SendToUser("/topic/getAllExternalPurchaseServicesNamesResult")
    public Response<List<String>> getAllExternalPurchaseServicesNames(SimpMessageHeaderAccessor headerAccessor) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllExternalPurchaseServicesNames();
    }

    private HistoryDTO convertToHistoryDTO1(History history) {
        return new HistoryDTO(history);
    }

    private StoreDTO convertToStoreDTO1(Store store) {
        return new StoreDTO(store);
    }

    private UserDTO convertToUserDTO1(User user) {
        return new UserDTO(user);
    }

    private ShoppingCartDTO convertToShoppingCartDTO1(List<ShoppingBasket> cartBaskets) {
        return new ShoppingCartDTO(cartBaskets);
    }

    private ItemDTO convertToItemDTO1(Item item, int amount) {
        return new ItemDTO(item, amount);
    }

    private PermissionDTO convertToPermissionDTO1(Permission p) {
        return new PermissionDTO(p);

    }

    private HistoryDTO convertToHistoryDTO(History history)
    {
        HistoryDTO dto_history = new HistoryDTO();
        dto_history.items = new HashSet<>();
        for (ItemHistory item : history.getHistoryItems())
        {
            ItemHistoryDTO dto_item = new ItemHistoryDTO();
            dto_item.product_name = item.product_name;
            dto_item.amount = item.amount;
            dto_item.username = item.username;
            dto_item.price_per_unit = item.price_per_unit;
            dto_item.store_id = item.store_id;
            dto_item.item_id = item.id;
            dto_item.date = item.date;
            dto_history.items.add(dto_item);
        }
        return dto_history;
    }

    private StoreDTO convertToStoreDTO(Store store)
    {
        StoreDTO dto_store = new StoreDTO();
        dto_store.name = store.getName();
        dto_store.id = store.getStoreId();
        dto_store.isOpen = store.isOpen();
        dto_store.permanentlyClosed = store.isPermanentlyClosed();
        dto_store.founder = store.getFounder();
        for(Map.Entry<Item, Integer> item_amount: store.getItems().entrySet())
        {
            ItemDTO item = convertToItemDTO(item_amount.getKey(), item_amount.getValue());
            Integer amount = item_amount.getValue();
            dto_store.items.put(item, amount);
        }
        dto_store.managers = store.getManagers();
        dto_store.owners = store.getOwners();
        return dto_store;
    }


    private UserDTO convertToUserDTO(User user)
    {
        UserDTO dto_user = new UserDTO();
        dto_user.userName = user.getName();
        dto_user.email = user.getEmail();
        dto_user.firstName = user.getFirstName();
        dto_user.lastName = user.getLastName();
        dto_user.shoppingCart = convertToShoppingCartDTO(user.getCartBaskets());
        return dto_user;
    }

    private ShoppingCartDTO convertToShoppingCartDTO(List<ShoppingBasket> cartBaskets)
    {
        ShoppingCartDTO shoppingCart = new ShoppingCartDTO();
        List<ShoppingBasketDTO> baskets = cartBaskets.stream().map(b -> convertToShoppingBasketDTO(b)).collect(Collectors.toList());
        shoppingCart.baskets = baskets;
        return shoppingCart;
    }
    private ShoppingBasketDTO convertToShoppingBasketDTO(ShoppingBasket basket){
        return new ShoppingBasketDTO(basket);
    }


    private ItemDTO convertToItemDTO(Item item, int amount)
    {
        ItemDTO item_dto = new ItemDTO();
        item_dto.item_id = item.getId();
        item_dto.rate = item.getRate();
        item_dto.price = item.getPrice();
        item_dto.product_name = item.getProductName();
        item_dto.category = item.getCategory().toString();
        item_dto.amount = amount;
        item_dto.keyWords = item.getKeyWords();
        return item_dto;
    }

    private PermissionDTO convertToPermissionDTO(Permission p) {
        PermissionDTO dto = new PermissionDTO();
        dto.permissionMask = p.getPermissionsMask();
        dto.givenBy = p.getGivenBy();
        return dto;
    }

    private DiscountDTO convertToDiscountDTO(SimpleDiscountPolicy sdp) {
        DiscountDTO dto = new DiscountDTO();
        dto.id = sdp.getId();
        dto.percentage = sdp.getPercentage();
        dto.displayString = sdp.display();
        return dto;
    }

    private PolicyDTO convertToPolicyDTO(SimplePurchasePolicy spp) {
        PolicyDTO dto = new PolicyDTO();
        dto.id = spp.getId();
        dto.hour = spp.getHour();
        dto.date = spp.getDate();
        dto.displayString = spp.display();
        return dto;
    }

    @MessageMapping("/market/getStoreInfo")
    @SendToUser("/topic/getStoreInfoResult")
    public Response<StoreDTO> getStore (SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<Store> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStore(map.get("id"));

        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        StoreDTO dtoStore = convertToStoreDTO(stores.getObject());

        return new Response<>(dtoStore);
    }

    @MessageMapping("/market/logout")
    @SendToUser("/topic/logoutResult")
    public Response<Boolean> logout(SimpMessageHeaderAccessor headerAccessor) {
        Response<Boolean> r = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).logout();
        return r;
    }

    @MessageMapping("/market/getStoreItems")
    @SendToUser("/topic/getStoreItemsResult")
    public Response<List<ItemDTO>> getStoreItems(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<Map<Item, Integer>> items = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getItems(map.get("id"));

        if (items.hadError())
            return new Response<>(items.getErrorMessage());

        List<ItemDTO> storeItems = items.getObject().entrySet().stream()
                .map(entry -> convertToItemDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());

        return new Response<>(storeItems);
    }


    @MessageMapping("/market/openNewStore")
    @SendToUser("/topic/openNewStoreResult")
    public Response<StoreDTO> openNewStore (SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {

        Response<Store> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore(map.get("name"));

        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        StoreDTO dtoStore = convertToStoreDTO(stores.getObject());

        return new Response<>(dtoStore);
    }

    @MessageMapping("/market/removeManager")
    @SendToUser("/topic/removeManagerResult")
    public Response<Boolean> removeManager(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeManager((String) map.get("toRemove"), (int) map.get("storeId"));
    }

    @MessageMapping("/market/addManager")
    @SendToUser("/topic/addManagerResult")
    public Response<Boolean> addManager(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addManager((String) map.get("manager"), (int) map.get("storeId"));
        return res;
    }


    @MessageMapping("/market/removeOwner")
    @SendToUser("/topic/removeOwnerResult")
    public Response<Boolean> removeOwner(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeOwner((String) map.get("toRemove"), (int) map.get("storeId"));
    }

    @MessageMapping("/market/addOwner")
    @SendToUser("/topic/addOwnerResult")
    public Response<Boolean> addOwner(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addOwner((String) map.get("owner"), (int) map.get("storeId"));
        return res;
    }

    @MessageMapping("/market/getManagersPermission")
    @SendToUser("/topic/getManagersPermissionResult")
    public Response<PermissionDTO> getManagersPermission(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Permission> resp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getManagersPermissions((int) map.get("storeId"), (String) map.get("manager"));
        if (resp.hadError()) {
            return new Response<>(resp.getErrorMessage());
        }
        return new Response<>(convertToPermissionDTO(resp.getObject()));
    }

    @MessageMapping("/market/setManagersPermission")
    @SendToUser("/topic/setManagersPermissionResult")
    public Response<Boolean> setManagersPermission(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).setManagerPermission((String) map.get("manager"), (int) map.get("storeId"), ((Integer) map.get("permissionMask")).byteValue());
    }

//    @MessageMapping("/market/openNewStore")
//    @SendToUser("/topic/getCartItemsResult")
//    public Response<List<ShoppingBasket>> getCartItemsResult (SimpMessageHeaderAccessor headerAccessor) {
//        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login("admin", "admin");
//
////        Response<Store> rStore1 = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("admin store");
////        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
////        Response<Item> rBanana = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
////        Response<Item> rOrange= ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "orange", Category.Food, 20, 9);
//
//
//        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToCart(rStore1.getObject().getStoreId(), rBanana.getObject().getId(), 2);
//        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToCart(rStore1.getObject().getStoreId(), rBanana.getObject().getId(), 2);
//
//        Response<List<ShoppingBasket>> shoppingBaskets = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getCartBaskets();
//
//        if (shoppingBaskets.hadError())
//            return new Response<>(shoppingBaskets.getErrorMessage());
//        //StoreDTO dtoStore = convertToStoreDTO(shoppingBaskets);
//
//        //return new Response<>(shoppingBaskets);
//        return shoppingBaskets;
//    }

    @MessageMapping("/market/addNewItem")
    @SendToUser("/topic/addNewItemResult")
    public Response<ItemDTO> addNewItem(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Item> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore((int) map.get("storeId"), (String) map.get("name"), (String) map.get("category"), Double.parseDouble((String) map.get("price")), Integer.parseInt((String) map.get("amount")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToItemDTO(res.getObject(), Integer.parseInt((String) map.get("amount"))));
    }

    @MessageMapping("/market/modifyItem")
    @SendToUser("/topic/modifyItemResult")
    public Response<ItemDTO> modifyItem(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        List<String> keywords = Arrays.asList(((String) map.get("keywords")).split("[\\s,]+"));
        Response<Item> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).modifyItem((int) map.get("storeId"), (Integer) map.get("itemId"), (String) map.get("name"), (String) map.get("category"), Double.parseDouble((String) map.get("price")), (Integer) map.get("amount"), keywords);
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToItemDTO(res.getObject(), (Integer) map.get("amount")));
    }


    @MessageMapping("/market/isAdmin")
    @SendToUser("/topic/isAdminResult")
    public Response<Boolean> isAdmin(SimpMessageHeaderAccessor headerAccessor) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).isLoggedInAdminCheck();
    }

    @MessageMapping("/market/getNotifications")
    @SendToUser("/topic/getNotificationsResult")
    public Response<List<INotification>> getNotifications(SimpMessageHeaderAccessor headerAccessor) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getUserNotifications();
    }

    @MessageMapping("/market/removeNotifications")
    @SendToUser("/topic/removeNotificationsResult")
    public Response<Boolean> removeNotifications(SimpMessageHeaderAccessor headerAccessor) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeUserNotifications();
    }

    @MessageMapping("/market/cart/inc")
    @SendToUser("/topic/cart/incResult")
    public Response<ItemDTO> incrementItemInCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Item> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToCart((int) map.get("storeId"), (Integer) map.get("itemId"), (Integer) map.get("amount"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToItemDTO(res.getObject(), (Integer) map.get("amount")));
    }

    @MessageMapping("/market/cart/getCart")
    @SendToUser("/topic/cart/getCartResult")
    public Response<ShoppingCartDTO> getCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {

        Response<List<ShoppingBasket>> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getCartBaskets();
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        List<ShoppingBasketDTO> basketDTOS = new LinkedList<>();
        for(ShoppingBasket basket: res.getObject()){
            Response<Map<Item, Double>> discounts_result = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getShoppingBasketDiscounts(basket);
            if (discounts_result.hadError()) {
                return new Response<>(discounts_result.getErrorMessage());
            }
            Response<String[]> store_name_result = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStoreNameByID(basket.getStoreId());
            if (store_name_result.hadError()) {
                return new Response<>(store_name_result.getErrorMessage());
            }
            basketDTOS.add(new ShoppingBasketDTO(basket,discounts_result.getObject() , store_name_result.getObject()[0]));
        }
        shoppingCartDTO.baskets = basketDTOS;
        return new Response<>(shoppingCartDTO);
    }
    @MessageMapping("/market/addDiscount")
    @SendToUser("/topic/addDiscountResult")
    public Response<DiscountDTO> addDiscount(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<SimpleDiscountPolicy> sdp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addDiscount((Integer) map.get("storeId"), Double.parseDouble((String) map.get("percentage")));
        if (sdp.hadError()) {
            return new Response<>(sdp.getErrorMessage());
        }
        return new Response<>(convertToDiscountDTO(sdp.getObject()));
    }

    @MessageMapping("/market/addPolicy")
    @SendToUser("/topic/addPolicyResult")
    public Response<PolicyDTO> addPolicy(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal  = Calendar.getInstance();
        try {
            cal.setTime(df.parse((String)map.get("date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Response<SimplePurchasePolicy> sdp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addPolicy((Integer) map.get("storeId"), Integer.parseInt((String) map.get("hour")), cal);
        if (sdp.hadError()) {
            return new Response<>(sdp.getErrorMessage());
        }
        return new Response<>(convertToPolicyDTO(sdp.getObject()));
    }

    @MessageMapping("/market/getAllDiscounts")
    @SendToUser("/topic/getAllDiscountsResult")
    public Response<List<DiscountDTO>> getAllDiscounts(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<List<SimpleDiscountPolicy>> sdp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllDiscountPolicies((Integer) map.get("storeId"));
        if (sdp.hadError()) {
            return new Response<>(sdp.getErrorMessage());
        }
        return new Response<>(sdp.getObject().stream().map(this::convertToDiscountDTO).collect(Collectors.toList()));
    }

    @MessageMapping("/market/getAllPurchasePolicies")
    @SendToUser("/topic/getAllPurchasePoliciesResult")
    public Response<List<PolicyDTO>> getAllPurchasePolicies(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<List<SimplePurchasePolicy>> spp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllPurchasePolicies((Integer) map.get("storeId"));
        if (spp.hadError()) {
            return new Response<>(spp.getErrorMessage());
        }
        return new Response<>(spp.getObject().stream().map(this::convertToPolicyDTO).collect(Collectors.toList()));
    }

    @MessageMapping("/market/removeDiscount")
    @SendToUser("/topic/removeDiscountResult")
    public Response<Boolean> removeDiscount(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeDiscount((Integer) map.get("storeId"), (Integer) map.get("discountId"));
    }

    @MessageMapping("/market/removePolicy")
    @SendToUser("/topic/removePolicyResult")
    public Response<Boolean> removePolicy(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removePolicy((Integer) map.get("storeId"), (Integer) map.get("policyId"));
    }

    @MessageMapping("/market/purchase")
    @SendToUser("/topic/purchaseResult")
    public Response<Boolean> getPurchase(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO(
                map.get("paymentServiceName"),
                map.get("card_number"),
                map.get("month"),
                map.get("year"),
                map.get("holder"),
                map.get("ccv"),
                map.get("id")
                );

        SupplyParamsDTO supplyParamsDTO = new SupplyParamsDTO(
                map.get("supplyServiceName"),
                map.get("name"),
                map.get("address"),
                map.get("city"),
                map.get("country"),
                map.get("zip")
        );

        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).purchaseShoppingCart(paymentParamsDTO, supplyParamsDTO);
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }


    @MessageMapping("/market/addItemPredicateToDiscount")
    @SendToUser("/topic/addItemPredicateToDiscountResult")
    public Response<DiscountDTO> addItemPredicateToDiscount(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<AbstractDiscountPolicy> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemPredicateToDiscount((Integer) map.get("storeId"), (Integer) map.get("discountId"), (String) map.get("discountType"), Integer.parseInt((String) map.get("itemId")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToDiscountDTO((SimpleDiscountPolicy) res.getObject()));
    }

    @MessageMapping("/market/addCategoryPredicateToDiscount")
    @SendToUser("/topic/addCategoryPredicateToDiscountResult")
    public Response<DiscountDTO> addCategoryPredicateToDiscount(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<AbstractDiscountPolicy> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addCategoryPredicateToDiscount((Integer) map.get("storeId"), (Integer) map.get("discountId"), (String) map.get("discountType"), (String) map.get("category"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToDiscountDTO((SimpleDiscountPolicy) res.getObject()));
    }

    @MessageMapping("/market/addBasketRequirementPredicateToDiscount")
    @SendToUser("/topic/addBasketRequirementPredicateToDiscountResult")
    public Response<DiscountDTO> addBasketRequirementPredicateToDiscount(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<AbstractDiscountPolicy> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addBasketRequirementPredicateToDiscount((Integer) map.get("storeId"), (Integer) map.get("discountId"), (String) map.get("discountType"), Double.parseDouble((String) map.get("minPrice")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToDiscountDTO((SimpleDiscountPolicy) res.getObject()));
    }

    @MessageMapping("/market/changePolicy")
    @SendToUser("/topic/changePolicyResult")
    public Response<Boolean> changePolicy(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal  = Calendar.getInstance();
        try {
            cal.setTime(df.parse((String)map.get("newDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).changePolicyHour((Integer) map.get("storeId"), (Integer) map.get("policyId"), Integer.parseInt((String) map.get("newHour")), cal);
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }

    @MessageMapping("/market/changeDiscountPercentage")
    @SendToUser("/topic/changeDiscountPercentageResult")
    public Response<Boolean> changeDiscountPercentage(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).changeDiscountPercentage((Integer) map.get("storeId"), (Integer) map.get("discountId"), Double.parseDouble((String) map.get("newPercentage")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }

    @MessageMapping("/market/cart/getPrice")
    @SendToUser("/topic/cart/getPriceResult")
    public Response<Double> getCaertPrice(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getCartPrice();

    }

    @MessageMapping("/market/addItemNotAllowedInDatePredicateToPolicy")
    @SendToUser("/topic/addItemNotAllowedInDatePredicateToPolicyResult")
    public Response<AbstractPurchasePolicy> addItemNotAllowedInDatePredicateToPolicy(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal  = Calendar.getInstance();
        try {
            cal.setTime(df.parse((String)map.get("date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Response<AbstractPurchasePolicy> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemNotAllowedInDatePredicateToPolicy((Integer) map.get("storeId"), (Integer) map.get("policyId"), (String) map.get("policyType") ,Integer.parseInt((String)map.get("itemId")), cal);
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }

    @MessageMapping("/market/addItemPredicateToPolicy")
    @SendToUser("/topic/addItemPredicateToPolicyResult")
    public Response<AbstractPurchasePolicy> addItemPredicateToPolicy(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<AbstractPurchasePolicy> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemPredicateToPolicy((Integer) map.get("storeId"), (Integer) map.get("policyId"), (String) map.get("policyType") ,Integer.parseInt((String)map.get("itemId")), Integer.parseInt((String)map.get("hour")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }
    @MessageMapping("/market/bid/addBid")
    @SendToUser("/topic/bid/addBidResult")
    public Response<Boolean> addBid(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addBid((Integer) map.get("store_id"), Double.parseDouble((String)map.get("bid_price")), (Integer) map.get("item_id"), (Integer) map.get("amount"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }

    @MessageMapping("/market/bid/addOwnerAgreement")
    @SendToUser("/topic/bid/addOwnerAgreementResult")
    public Response<Boolean> addOwnerAgreement(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addOwnerAgreement((String) map.get("owner"), (Integer) map.get("store_id"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return res;
    }

    @MessageMapping("/market/bid/deleteBid")
    @SendToUser("/topic/bid/deleteBidResult")
    public Response<BidDTO> deleteBid(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Bid> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).deleteBid((Integer) map.get("store_id"), (Integer) map.get("bid_id"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(new BidDTO(res.getObject()));
    }

    @MessageMapping("/market/bid/deleteOwnerAgreement")
    @SendToUser("/topic/bid/deleteOwnerAgreementResult")
    public Response<OwnerAgreementDTO> deleteOwnerAgreement(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<OwnerAgreement> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).deleteAgreement((Integer) map.get("store_id"), (String) map.get("owner_name"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(new OwnerAgreementDTO(res.getObject()));
    }
    @MessageMapping("/market/bid/approveBid")
    @SendToUser("/topic/bid/approveBidResult")
    public Response<BidDTO> approveBid(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Bid> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).approveBid((Integer) map.get("store_id"), (Integer) map.get("bid_id"));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(new BidDTO(res.getObject()));
    }
    @MessageMapping("/market/bid/approveAllBids")
    @SendToUser("/topic/bid/approveAllBidsResult")
    public Response<Boolean> approveAllBids(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).approveAllBids((Integer) map.get("store_id"));
    }
    @MessageMapping("/market/bid/updateBid")
    @SendToUser("/topic/bid/updateBidResult")
    public Response<BidDTO> updateBid(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Bid> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).updateBid((Integer) map.get("store_id"), (Integer) map.get("bid_id"), Double.parseDouble((String) map.get("bid_price")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(new BidDTO(res.getObject()));
    }
    @MessageMapping("/market/bid/addBidToCart")
    @SendToUser("/topic/bid/addBidToCartResult")
    public Response<Boolean> addBidToCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addBidToCart((Integer) map.get("bid_id"));
    }
    @MessageMapping("/market/bid/getBids")
    @SendToUser("/topic/bid/getBidsResult")
    public Response<Set<BidDTO>> getBids(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Collection<Bid>> res =  ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getBids((Integer) map.get("store_id"));
        if(res.hadError())
            return new Response<>(res.getErrorMessage());
        Collection<Bid> bids = res.getObject();
        return new Response<>(bids.stream().map(BidDTO::new).collect(Collectors.toSet()));
    }

    @MessageMapping("/market/bid/getUserBids")
    @SendToUser("/topic/bid/getUserBidsResult")
    public Response<Set<BidDTO>> getUserBids(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Collection<Bid>> res =  ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getUserBids();
        if(res.hadError())
            return new Response<>(res.getErrorMessage());
        Collection<Bid> bids = res.getObject();
        Set<BidDTO> b = bids.stream().map(BidDTO::new).collect(Collectors.toSet());
        return new Response<Set<BidDTO>>(b);
    }

    @MessageMapping("/market/getStats")
    @SendToUser("/topic/getStatsResult")
    public Response<List<StatsDTO>> getStats(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<List<Map.Entry<LocalDate, Stats>>> res =  ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStats();
        if(res.hadError())
            return new Response<>(res.getErrorMessage());
        return new Response(res.getObject().stream().map(e -> new StatsDTO(e.getKey(), e.getValue())));
    }
}