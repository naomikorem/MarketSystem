package DomainLayer.Stores;

import DataLayer.DALObjects.ItemDAL;
import DataLayer.ItemManager;
import Utility.LogUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Item {


    private int id;
    private String product_name;
    private Category category;
    private double rate; //each product can be rated by clients
    private int numberOfRatings; // amount of people rated
    private double price;
    private List<String> keyWords;
    private static int TOP_RATE = 5;
    private static int BOTTOM_RATE = 0;


    public Item() {}

    public Item(String product_name, Category category, double price) {
        checkParams(product_name, price);
        this.id = 0;
        this.product_name = product_name;
        this.category = category;
        this.price = price;
        this.rate = 0;
        this.numberOfRatings = 0;
        keyWords = new ArrayList<>();
    }

    public void updateItem(String productName, Category category, double price, List<String> keywords) {
        String oldname = this.product_name;
        Category oldC = this.category;
        double oldPrice = this.price;
        List<String> oldKeywords = this.keyWords;

        checkParams(productName, price);
        this.product_name = productName;
        this.category = category;
        this.price = price;
        this.keyWords = keywords;

        try {
            update();
        } catch (Exception e) {
            this.product_name = oldname;
            this.category = oldC;
            this.price = oldPrice;
            this.keyWords = oldKeywords;
        }
    }

    public void checkParams(String product_name, double price) {
        if (product_name == null || product_name.equals("")) {
            LogUtility.warn("tried to change product name to an empty word / null");
            throw new IllegalArgumentException("Product name must be a non empty name");
        }
        if (price < 0) {
            LogUtility.warn("tried to change product price to a negative value");
            throw new IllegalArgumentException("Product price must not be below 0");
        }
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {

        checkParams(product_name, price);
        this.product_name = product_name;
        LogUtility.info(String.format("Item %d name was updated to %s",this.id, product_name));
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getRate() {
        return rate;
    }

    public void updateRate(double new_rate) {
        double oldRate = this.rate;

        if (new_rate > TOP_RATE || new_rate < BOTTOM_RATE) {
            if (new_rate > TOP_RATE)
                LogUtility.warn("tried to add a new rate for a number bigger then 5");
            else
                LogUtility.warn("tried to add a new rate for a number lower then 0");
            throw new IllegalArgumentException("Product rate must be between 0-5");
        }
        this.rate = (this.rate * this.numberOfRatings + new_rate) / (this.numberOfRatings + 1);
        this.numberOfRatings++;

        try {
            update();
            LogUtility.info(String.format("Item %d Rate was updated",this.id));
        } catch (Exception e) {
            this.rate = oldRate;
            this.numberOfRatings--;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            LogUtility.warn("tried to change product price to a negative value");
            throw new IllegalArgumentException("Product price must be a non negative number");
        }
        this.price = price;
    }

    public boolean isNameContains(String name) {
        return this.product_name.contains(name);
    }

    public boolean hasKeyWords(List<String> kws) {
        for (String kw : kws)
            for (String itemKW : keyWords)
                if (itemKW.contains(kw))
                    return true;
        return false;
    }


    public List<String> getKeyWords() {
        return new ArrayList<>(keyWords);
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public int getId() {
        return this.id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemDAL toDAL() {
        ItemDAL res = new ItemDAL();
        res.setRate(getRate());
        res.setId(getId());
        res.setPrice(getPrice());
        res.setCategory(getCategory());
        res.setProduct_name(getProduct_name());
        res.setNumberOfRatings(getNumberOfRatings());
        res.setKeyWords(getKeyWords());
        return res;
    }

    private void update() {
        if (getId() != 0) {
            ItemManager.getInstance().addObject(this.toDAL());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && product_name.equals(item.product_name) && category == item.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_name, category);
    }
}
