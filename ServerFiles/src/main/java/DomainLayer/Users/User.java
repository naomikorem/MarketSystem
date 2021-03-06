package DomainLayer.Users;

import DataLayer.DALObjects.UserDAL;
import DomainLayer.Observer;
import DomainLayer.Response;
import DomainLayer.Stores.Bid;
import DomainLayer.Stores.Item;
import DomainLayer.SystemManagement.NotificationManager.INotification;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class User implements Observer {
    private UserState state;
    private ShoppingCart shoppingCart;
    private String sessionId;
    private SimpMessagingTemplate template;


    public User(UserState state) {
        this.state = state;
        this.shoppingCart = new ShoppingCart();
    }

    private MessageHeaders createHeaders() {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(getSessionId());
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();

    }
    public Collection<Bid> getBids(){
        return shoppingCart.getBids();
    }
    public Collection<Bid> getBidsInCart(){
        return shoppingCart.getBids().stream().filter(Bid::isInCart).collect(Collectors.toSet());
    }
    public void addBid(Bid bid){
        shoppingCart.addBid(bid);
    }
    public void removeBid(int bidId){
        shoppingCart.removeBid(bidId);
    }
    public Bid addBidToCart(int bidId){
        return shoppingCart.addBidToCart(bidId);
    }
    public Bid getBid(int bidId){
        return shoppingCart.getBid(bidId);
    }

    public void sendNotification(INotification notification) {
        if(getSessionId() == null || getTemplate() == null)
            return;
        String session = getSessionId();
        getTemplate().convertAndSendToUser(session, "/topic/notificationResult", new Response<>(notification), createHeaders());
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public SimpMessagingTemplate getTemplate() {
        return this.template;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getName() {
        return state.getName();
    }

    @Override
    public List<INotification> updateNotifications()
    {
        // TODO: change implementation
        return null;
    }

    public String getEmail() {
        return state.getEmail();
    }

    public boolean isSubscribed() {
        return state.isSubscribed();
    }

    public boolean login(String password) {
        return state.login(password);
    }

    public List<Item> getShoppingCartItems () {
        return shoppingCart.getAllItems();
    }
    public List<ShoppingBasket> getCartBaskets() {
        return shoppingCart.getBaskets();
    }

    public void addItemToShoppingCart(int storeId, Item item, int amount) {
        this.shoppingCart.addItem(storeId, item, amount);
    }

    public UserState getState() {
        return this.state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public void setName(String name){
        this.state.setName(name);
    }

    public void setEmail(String email){
        this.state.setEmail(email);
    }

    public void addManagedStore(int storeId) {
        this.state.addManagedStore(storeId);
    }

    public void addOwnedStore(int storeId) {
        this.state.addOwnedStore(storeId);
    }

    public void removedManagedStore(int storeId) {
        this.state.removeManagedStore(storeId);
    }

    public void removedOwnedStore(int storeId) {
        this.state.removeOwnedStore(storeId);
    }

    public Set<Integer> getManagedStores() {
        return this.state.getManagedStores();
    }

    public Set<Integer> getOwnedStores() {
        return this.state.getOwnedStores();
    }

    public void emptyShoppingCart() {
        this.shoppingCart.emptyShoppingCart();
        this.shoppingCart.emptyBidsInCart();
    }

    public String getFirstName() {
        return this.state.getFirstName();
    }

    public String getLastName() {
        return this.state.getLastName();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || ((other instanceof User) &&
                (((User) other).isSubscribed() && ((User) other).getName().equals(getName())));
    }

    @Override
    public int hashCode() {
        if (isSubscribed()) {
            return Objects.hashCode(this.getName());
        }
        return Objects.hashCode(state);
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public UserDAL toDAL() {
        if (!isSubscribed()) {
            throw new IllegalArgumentException("Cannot turn guest user into dal.");
        }
        UserDAL res = new UserDAL();
        res.setFirstName(getFirstName());
        res.setPassword(getState().getPassword());
        res.setEmail(getEmail());
        res.setUserName(getName());
        res.setLastName(getLastName());
        res.setOwnedStores(getOwnedStores());
        res.setManagedStores(getManagedStores());
        res.setShoppingBaskets(shoppingCart.getBaskets().stream().map(ShoppingBasket::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
