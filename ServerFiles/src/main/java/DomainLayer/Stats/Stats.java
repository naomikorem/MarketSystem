package DomainLayer.Stats;

import DataLayer.DALObjects.StatisticsDAL;
import DomainLayer.Users.AdminController;
import DomainLayer.Users.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Stats {
    private Set<String> guests;
    private Set<User> visitors;

    public Stats() {
        this.guests = new HashSet<>();
        this.visitors = new HashSet<>();
    }

    public Stats(Set<String> guests, Set<User> visitors) {
        this.guests = guests;
        this.visitors = visitors;
    }

    public void addGuest(String guestAddr) {
        this.guests.add(guestAddr);
    }

    public void removeGuest(String guestAddr) {
        //can happen when guest user logs in
        this.guests.remove(guestAddr);
    }

    public void addUser(User u) {
        this.visitors.add(u);
    }

    public long guestsCount() {
        return this.guests.size();
    }

    public long regularUsersCount() {
        return this.visitors.stream().filter(u -> u.getOwnedStores().isEmpty() && u.getManagedStores().isEmpty()).count();
    }

    public long managerUsersCount() {
        return this.visitors.stream().filter(u -> u.getOwnedStores().isEmpty() && !u.getManagedStores().isEmpty()).count();
    }

    public long ownerUsersCount() {
        return this.visitors.stream().filter(u -> !u.getOwnedStores().isEmpty()).count();
    }

    public long adminUsersCount() {
        return this.visitors.stream().filter(u -> AdminController.getInstance().isAdmin(u.getName())).count();
    }

    public StatisticsDAL toDAL(LocalDate date) {
        StatisticsDAL res = new StatisticsDAL();
        res.setId(date);
        res.setGuests(this.guests);
        res.setVisitors(this.visitors.stream().map(User::toDAL).collect(Collectors.toSet()));
        return res;
    }
}
