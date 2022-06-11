package DataLayer.DALObjects;

import DataLayer.DALObject;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "ShoppingBasket")
public class ShoppingBasketDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int storeId;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "itemsInBaskets", joinColumns = @JoinColumn(name = "basketId"))
    @MapKeyJoinColumn(name = "itemId")
    @Column(name = "amount")
    private Map<ItemDAL, Integer> items;

    public ShoppingBasketDAL() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Map<ItemDAL, Integer> getItems() {
        return items;
    }

    public void setItems(Map<ItemDAL, Integer> items) {
        this.items = items;
    }

    public ShoppingBasket toDomain() {
        Map<Item, Integer> items = new HashMap<>();
        getItems().forEach((key, value) -> items.put(key.toDomain(), value));
        ShoppingBasket sb = new ShoppingBasket(getStoreId(), items);
        sb.setId(getId());
        return sb;
    }
}
