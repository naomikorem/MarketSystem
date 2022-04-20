package DomainLayer.Stores;

public class Item {
    private String product_name;
    private Category category;
    private double rate; //each product can be rated by clients
    private int numberOfRatings; // amount of people rated
    private double price;

    public Item(String product_name, Category category, double price) {
        this.product_name = product_name;
        this.category = category;
        this.price = price;
        this.rate = 0;
        this.numberOfRatings = 0;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        if (product_name == null || product_name.equals("")) {
            Logger.LogUtility.error("tried to change product name to an empty word / null");
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
                Logger.LogUtility.error("tried to add a new rate for a number bigger then 5");
            else
                Logger.LogUtility.error("tried to add a new rate for a number lower then 0");
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
            Logger.LogUtility.error("tried to change product price to a negative value");
            throw new IllegalArgumentException("Product price must be a non negative number");
        }
        this.price = price;
    }
}
