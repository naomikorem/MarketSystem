package DomainLayer;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.User;

import java.util.Collection;
import java.util.List;

public class SystemImplementor implements SystemInterface {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private MarketManagementFacade marketManagementFacade;
    private User user;

    //Add catch to every function here.

    public SystemImplementor() {
        this.userFacade = new UserFacade();
        this.storeFacade = new StoreFacade();
        this.marketManagementFacade = MarketManagementFacade.getInstance();
    }

    public Response<Boolean> enter() {
        try {
            if (this.user != null) {
                return new Response<>(false);
            }
            this.user = new User(new GuestState());
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Boolean> exit() {
        try {
            if (this.user != null) {
                logout();
            }
            this.user = null;
            return new Response<>(true);
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<User> register(String email, String name, String password) {
        return userFacade.register(email, name, password);
    }

    public Response<User> login(String name, String password) {
        Response<User> r = userFacade.login(name, password);
        if (!r.hadError()) {
            this.user = r.getObject();
        }
        return r;
    }

    public Response<Boolean> logout() {
        if (this.user == null || !this.user.isLoggedIn()) {
            return new Response<>("You have to be logged in to perform this action.");
        }
        Response<Boolean> res = userFacade.logout(user.getName());
        if (res.getObject()) {
            this.user = new User(new GuestState());
        }
        return res;
    }


    public void addManager(User owner, String manager, int storeId) {
        if (userFacade.isExist(manager)) {
            throw new IllegalArgumentException(String.format("There is no user by the name of %s", manager));
        }
        storeFacade.addManager(owner, manager, storeId);
    }

    @Override
    public Response<Collection<Store>> getAllStores() {
        return storeFacade.getAllStores();
    }

    @Override
    public Response<Store> getStore(int id) {
        return storeFacade.getStore(id);
    }

    @Override
    public Response<List<Item>> getShoppingCartItems() {
        try {
            return new Response<>(user.getShoppingCartItems());
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    @Override
    public Response<Item> addItemToCart(int storeId, int itemId) {
        try {
            Response<Item> itemRes = storeFacade.getItemFromStore(storeId, itemId);
            if (itemRes.hadError()) {
                return itemRes;
            }
            user.addItemToShoppingCart(storeId, itemRes.getObject());
            return itemRes;
        } catch (Exception e) {
            return new Response<>(e.getMessage());
        }
    }

    public Response<Store> addNewStore(String name) {
        return storeFacade.addNewStore(user, name);
    }

    public Response<Boolean> initializeMarket()
    {
        // check if there is system manager
        if(!userFacade.hasAdmin())
        {
            // create the first system admin if there is no system manager
           Response<Boolean> res = userFacade.addAdmin("stub", "stub", "stub");
            if (res.hadError()) {
                return res;
            }
        }
        return this.marketManagementFacade.initializeMarket();
    }
    public Response<Boolean> addExternalPurchaseService(String name) {
        return this.marketManagementFacade.addExternalPurchaseService(name);
    }

    public Response<Boolean> addExternalSupplyService(String name) {
        return this.marketManagementFacade.addExternalSupplyService(name);
    }

    public Response<Boolean> removeExternalPurchaseService(String name) {
        return this.marketManagementFacade.removeExternalPurchaseService(name);
    }
}
