package ServiceLayer;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Permission;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Service {

    public static final String SYSTEM_IMPLEMENTOR_STRING = "SystemImplementor";

    public Service() {
        super();
    }

//    @MessageMapping("/market/echo/{var1}")
//    public void test(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String var1) {
//        System.out.println(headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING));
//    }

    @MessageMapping("/market/login")
    @SendToUser("/topic/loginResult")
    public Response<UserDTO> login(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login(map.get("user"), map.get("pass"));
        if (user.hadError())
            return new Response<>(user.getErrorMessage());
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
        if (user.hadError())
            return new Response<>(user.getErrorMessage());
        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/register")
    @SendToUser("/topic/registerResult")
    public Response<UserDTO> register(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<User> user = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).
                register(map.get("email"), map.get("username"),  map.get("firstname"), map.get("lastname"), map.get("pass"));
        if (user.hadError())
            return new Response<>(user.getErrorMessage());
        return new Response<>(convertToUserDTO(user.getObject()));
    }

    @MessageMapping("/market/getStores")
    @SendToUser("/topic/getStoresResult")
    public Response<List<StoreDTO>> getStores (SimpMessageHeaderAccessor headerAccessor) {

        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).register("store@gmail.com", "store", "store", "store", "store");
        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login("store", "store");
        Response<Store> rStore1 = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("store");
        Response<Store> rStore2 = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("another store");

        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
        Response<Item> rBanana = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
        Response<Item> rOrange= ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "orange", Category.Food, 20, 9);
        Response<Item> rApple = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "apple", Category.Food, 10, 2);
        Response<Item> rShirt= ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "shirt", Category.Clothing, 20, 3);



        Response<Collection<Store>> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllStores();
        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        List<StoreDTO> dto_stores = stores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }

    @MessageMapping("/market/getOpenStores")
    @SendToUser("/topic/getOpenStoresResult")
    public Response<List<StoreDTO>> getOpenStores (SimpMessageHeaderAccessor headerAccessor) {

        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).register("store@gmail.com", "store", "store", "store", "store");
        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login("store", "store");
        Response<Store> rStore1 = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("store");
        Response<Store> rStore2 = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("another store");

        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).closeStore(rStore2.getObject().getStoreId());

        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
        Response<Item> rBanana = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "banana", Category.Food, 55, 5);
        Response<Item> rOrange= ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "orange", Category.Food, 20, 9);
        Response<Item> rApple = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "apple", Category.Food, 10, 2);
        Response<Item> rShirt= ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore(rStore1.getObject().getStoreId(), "shirt", Category.Clothing, 20, 3);


        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).logout();


        Response<Collection<Store>> openStores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getAllOpenStores();

        if (openStores.hadError())
            return new Response<>(openStores.getErrorMessage());
        List<StoreDTO> dto_stores = openStores.getObject().stream().map(this::convertToStoreDTO).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }

    @MessageMapping("/market/getStoresBesidesPermanentlyClosed")
    @SendToUser("/topic/getStoresBesidesPermanentlyClosedResult")
    public Response<List<StoreDTO>> getStoresBesidesPermanentlyClosed (SimpMessageHeaderAccessor headerAccessor) {
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

    @MessageMapping("/market/RemoveItemFromCart")
    @SendToUser("/topic/removeItemFromCartResult")
    public Response<Boolean> RemoveItemFromCart(SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING))
                .removeItemFromCart((Integer)map.get("store_id"),(Item) map.get("item_id"), (Integer) map.get("amount"));
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
        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).purchaseShoppingCart("ashdod", AbstractProxy.GOOD_STUB_NAME, AbstractProxy.GOOD_STUB_NAME);

        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getPurchaseHistory();
        if(history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
    }

    @MessageMapping("/market/getUserHistory")
    @SendToUser("/topic/getUserHistoryResult")
    public Response<HistoryDTO> getUserPurchaseHistory(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getPurchaseHistory(map.get("username"));
        if(history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
    }

    @MessageMapping("/market/getStoreHistory")
    @SendToUser("/topic/getStoreHistoryResult")
    public Response<HistoryDTO> getStorePurchaseHistory(SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
        Response<History> history = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getStoreHistory(map.get("storeId"));
        if(history.hadError())
            return new Response<>(history.getErrorMessage());

        return new Response<>(convertToHistoryDTO(history.getObject()));
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

    private ShoppingBasketDTO convertToShoppingBasketDTO(ShoppingBasket basket)
    {
        ShoppingBasketDTO basket_dto = new ShoppingBasketDTO();
        basket_dto.Store_id = basket.getStoreId();
        for(Map.Entry<Item, Integer> item_amount: basket.getItemsAndAmounts())
        {
            ItemDTO item = convertToItemDTO(item_amount.getKey(), item_amount.getValue());
            Integer amount = item_amount.getValue();
            basket_dto.items_and_amounts.put(item, amount);
        }
        return basket_dto;
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
    public Response<Boolean> logout (SimpMessageHeaderAccessor headerAccessor) {


        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).logout();
    }

    @MessageMapping("/market/getStoreItems")
    @SendToUser("/topic/getStoreItemsResult")
    public Response<List<ItemDTO>> getStoreItems (SimpMessageHeaderAccessor headerAccessor, Map<String, Integer> map) {
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
        //((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login("admin", "admin");
        Response<Store> stores = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore(map.get("name"));

        if (stores.hadError())
            return new Response<>(stores.getErrorMessage());
        StoreDTO dtoStore = convertToStoreDTO(stores.getObject());

        return new Response<>(dtoStore);
    }

    @MessageMapping("/market/removeManager")
    @SendToUser("/topic/removeManagerResult")
    public Response<Boolean> removeManager (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeManager((String) map.get("toRemove"), (int) map.get("storeId"));
    }

    @MessageMapping("/market/addManager")
    @SendToUser("/topic/addManagerResult")
    public Response<Boolean> addManager (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addManager((String) map.get("manager"), (int) map.get("storeId"));
        return res;
    }


    @MessageMapping("/market/removeOwner")
    @SendToUser("/topic/removeOwnerResult")
    public Response<Boolean> removeOwner (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).removeOwner((String) map.get("toRemove"), (int) map.get("storeId"));
    }

    @MessageMapping("/market/addOwner")
    @SendToUser("/topic/addOwnerResult")
    public Response<Boolean> addOwner (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Boolean> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addOwner((String) map.get("owner"), (int) map.get("storeId"));
        return res;
    }

    @MessageMapping("/market/getManagersPermission")
    @SendToUser("/topic/getManagersPermissionResult")
    public Response<PermissionDTO> getManagersPermission (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Permission> resp = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).getManagersPermissions((int) map.get("storeId"), (String) map.get("manager"));
        if (resp.hadError()) {
            return new Response<>(resp.getErrorMessage());
        }
        return new Response<>(convertToPermissionDTO(resp.getObject()));
    }

    @MessageMapping("/market/setManagersPermission")
    @SendToUser("/topic/setManagersPermissionResult")
    public Response<Boolean> setManagersPermission (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
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
    public Response<ItemDTO> addNewItem (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        Response<Item> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addItemToStore((int) map.get("storeId"), (String) map.get("name"), (String) map.get("category"), Double.parseDouble((String) map.get("price")), Integer.parseInt((String) map.get("amount")));
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToItemDTO(res.getObject(), Integer.parseInt((String) map.get("amount"))));
    }

    @MessageMapping("/market/modifyItem")
    @SendToUser("/topic/modifyItemResult")
    public Response<ItemDTO> modifyItem (SimpMessageHeaderAccessor headerAccessor, Map<String, Object> map) {
        List<String> keywords = Arrays.asList(((String) map.get("keywords")).split("[\\s,]+"));
        Response<Item> res = ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).modifyItem((int) map.get("storeId"), (Integer) map.get("itemId"),(String) map.get("name"), (String) map.get("category"), Double.parseDouble((String) map.get("price")), (Integer) map.get("amount"), keywords);
        if (res.hadError()) {
            return new Response<>(res.getErrorMessage());
        }
        return new Response<>(convertToItemDTO(res.getObject(), (Integer) map.get("amount")));
    }

}
