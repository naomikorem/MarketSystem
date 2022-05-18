package ServiceLayer;

import DomainLayer.Response;
import DomainLayer.Stores.Category;
import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.User;
import ServiceLayer.DTOs.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login("store", "store");
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
        List<StoreDTO> dto_stores = stores.getObject().stream().map(s-> convertToStoreDTO(s)).collect(Collectors.toList());

        return new Response<>(dto_stores);
    }



    private StoreDTO convertToStoreDTO(Store store)
    {
        StoreDTO dto_store = new StoreDTO();
        dto_store.name = store.getName();
        dto_store.id = store.getStoreId();
        dto_store.isOpen = store.isOpen();
        dto_store.founder = store.getFounder();
        for(Map.Entry<Item, Integer> item_amount: store.getItems().entrySet())
        {
            ItemDTO item = convertToItemDTO(item_amount.getKey(), item_amount.getValue());
            Integer amount = item_amount.getValue();
            dto_store.items.put(item, amount);
        }
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
        return item_dto;
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
}
