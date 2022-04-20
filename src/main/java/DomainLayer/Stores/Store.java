package main.java.DomainLayer.Stores;

public class Store {
    private String store_name;
    private boolean is_open;
    private int id;

    public Store(String store_name, int id) {
        this.store_name = store_name;
        this.is_open = true;
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        if (store_name == null || store_name.equals("")) {
            Logger.LogUtility.error("tried to change store name to an empty word / null");
            throw new IllegalArgumentException("Store name must be a non empty name");
        }
        this.store_name = store_name;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public int getId() {
        return id;
    }

}
