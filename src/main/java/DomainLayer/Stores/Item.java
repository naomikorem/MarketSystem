package DomainLayer.Stores;

import Utility.LogUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Item {

    private static int NEXT_ITEM_ID = 1;

    private int id;
    private String product_name;
    private Category category;
    private double rate; //each product can be rated by clients
    private int numberOfRatings; // amount of people rated
    private double price;
    private List<String> keyWords;

    public int getNextItemId() {
        return NEXT_ITEM_ID++;
    }

    public Item(String product_name, Category category, double price) {
        this.id = getNextItemId();
        this.product_name = product_name;
        this.category = category;
        this.price = price;
        this.rate = 0;
        this.numberOfRatings = 0;
        keyWords = new ArrayList<>();
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        if (product_name == null || product_name.equals("")) {
            LogUtility.error("tried to change product name to an empty word / null");
            throw new IllegalArgumentException("Product name must be a non empty name");
        }
        this.product_name = product_name;
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
        if (new_rate > 5 || new_rate < 0) {
            if (new_rate > 5)
                LogUtility.error("tried to add a new rate for a number bigger then 5");
            else
                LogUtility.error("tried to add a new rate for a number lower then 0");
            throw new IllegalArgumentException("Product rate must be between 0-5");
        }
        this.rate = (this.rate * this.numberOfRatings + new_rate) / (this.numberOfRatings + 1);
        this.numberOfRatings++;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            LogUtility.error("tried to change product price to a negative value");
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
}
