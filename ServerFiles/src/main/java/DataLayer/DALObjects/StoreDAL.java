package DataLayer.DALObjects;

import DataLayer.DALObject;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "stores")
public class StoreDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean open;
    private String founder;
    private boolean permanentlyClosed;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discountPolicy")
    private CompositeDiscountDAL discount;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchasePolicy")
    private CompositePurchasePolicyDAL purchase;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "ItemsInStores", joinColumns = @JoinColumn(name = "storeId"))
    @MapKeyJoinColumn(name = "itemId")
    @Column(name = "amount")
    private Map<ItemDAL, Integer> items;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "storeOwners", joinColumns = @JoinColumn(name = "store"))
    @MapKeyJoinColumn(name = "owner")
    @Column(name = "givenBy")
    private Map<UserDAL, String> owners;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private Set<PermissionDAL> managers;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public boolean isPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setPermanentlyClosed(boolean permanentlyClosed) {
        this.permanentlyClosed = permanentlyClosed;
    }

    public CompositeDiscountDAL getDiscount() {
        return discount;
    }

    public void setDiscount(CompositeDiscountDAL discount) {
        this.discount = discount;
    }

    public CompositePurchasePolicyDAL getPurchase() {
        return purchase;
    }

    public void setPurchase(CompositePurchasePolicyDAL purchase) {
        this.purchase = purchase;
    }

    public Map<ItemDAL, Integer> getItems() {
        return items;
    }

    public void setItems(Map<ItemDAL, Integer> items) {
        this.items = items;
    }

    public Map<UserDAL, String> getOwners() {
        return owners;
    }

    public void setOwners(Map<UserDAL, String> owners) {
        this.owners = owners;
    }

    public Set<PermissionDAL> getManagers() {
        return managers;
    }

    public void setManagers(Set<PermissionDAL> managers) {
        this.managers = managers;
    }
}
