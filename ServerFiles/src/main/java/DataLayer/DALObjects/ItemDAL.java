package DataLayer.DALObjects;


import DomainLayer.Stores.Category;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Items")
public class ItemDAL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String product_name;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category")
    private Category category;
    private double rate; //each product can be rated by clients
    private int numberOfRatings; // amount of people rated
    private double price;
    @ElementCollection
    @CollectionTable(name="keywords", joinColumns=@JoinColumn(name="itemId"))
    @Column(name="keyword")
    private List<String> keyWords;

    public ItemDAL(int id, String product_name, Category category, double rate, int numberOfRatings, double price, List<String> keyWords) {
        this.id = id;
        this.product_name = product_name;
        this.category = category;
        this.rate = rate;
        this.numberOfRatings = numberOfRatings;
        this.price = price;
        this.keyWords = keyWords;
    }


    public ItemDAL() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
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

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}
