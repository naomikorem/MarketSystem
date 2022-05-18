package ServiceLayer;

import DomainLayer.Response;
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
        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("store");
        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).addNewStore("another store");


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
            ItemDTO item = convertToItemDTO(item_amount.getKey());
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
            ItemDTO item = convertToItemDTO(item_amount.getKey());
            Integer amount = item_amount.getValue();
            basket_dto.items_and_amounts.put(item, amount);
        }
        return basket_dto;
    }

    private ItemDTO convertToItemDTO(Item item)
    {
        ItemDTO item_dto = new ItemDTO();
        item_dto.item_id = item.getId();
        item_dto.rate = item.getRate();
        item_dto.price = item.getPrice();
        item_dto.product_name = item.getProductName();
        item_dto.category = item.getCategory().toString();
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


}
