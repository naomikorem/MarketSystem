package main.java.DomainLayer.Stores;

import java.util.Map;

public class StoreController {

    private static StoreController storeController;

    private Map<Integer, Store> stores; // store-id , stores

    private StoreController() {
    }

    public StoreController getInstance() {
        if (storeController == null) {
            storeController = new StoreController();
        }
        return storeController;
    }

    public Store getStore(int id){
        return stores.getOrDefault(id, null);
    }

}
