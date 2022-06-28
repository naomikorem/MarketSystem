package DataLayer.DALObjects;

import DataLayer.DALObject;
import DomainLayer.Stats.Stats;
import DomainLayer.Stores.Item;
import DomainLayer.Users.ShoppingBasket;
import DomainLayer.Users.UserController;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Statistics")
public class StatisticsDAL implements DALObject<LocalDate> {
    @Id
    private LocalDate date;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="statisticsGuests", joinColumns=@JoinColumn(name="statistic"))
    @Column(name="guestIp")
    private Set<String> guests;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "UsersInStatistics",
            joinColumns = {@JoinColumn(name = "statisticId")},
            inverseJoinColumns = {@JoinColumn(name = "userName")})
    private Set<UserDAL> visitors;


    @Override
    public LocalDate getId() {
        return this.date;
    }

    @Override
    public void setId(LocalDate id) {
        this.date = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<String> getGuests() {
        return guests;
    }

    public void setGuests(Set<String> guests) {
        this.guests = guests;
    }

    public Set<UserDAL> getVisitors() {
        return visitors;
    }

    public void setVisitors(Set<UserDAL> visitors) {
        this.visitors = visitors;
    }

    public Stats toDomain() {
        return new Stats(this.guests, this.visitors.stream().map(u -> UserController.getInstance().getUser(u.getId())).collect(Collectors.toSet()));
    }
}
