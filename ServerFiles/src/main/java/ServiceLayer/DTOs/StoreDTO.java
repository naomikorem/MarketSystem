package ServiceLayer.DTOs;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreDTO {
    public String name;
    public boolean isOpen;
    public boolean permanentlyClosed;
    public int id;
    public String founder;
    public List<String> owners;
    public List<String> managers;
    public Map<ItemDTO, Integer> items;

    public StoreDTO(Store store) {
        this.owners = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.items = new HashMap<>();
        this.name = store.getName();
        this.id = store.getStoreId();
        this.isOpen = store.isOpen();
        this.permanentlyClosed = store.isPermanentlyClosed();
        this.founder = store.getFounder();
        for (Map.Entry<Item, Integer> item_amount : store.getItems().entrySet()) {
            ItemDTO item = new ItemDTO(item_amount.getKey(), item_amount.getValue());
            Integer amount = item_amount.getValue();
            this.items.put(item, amount);
        }
        this.managers = store.getManagers();
        this.owners = store.getOwners();
    }

    public StoreDTO() {
        this.owners = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.items = new HashMap<>();
    }
}
