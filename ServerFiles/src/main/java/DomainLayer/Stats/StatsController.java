package DomainLayer.Stats;

import DomainLayer.Users.User;
import DomainLayer.Users.UserController;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatsController {
    private Map<LocalDate, Stats> dateToStats;

    public StatsController() {
        this.dateToStats = new HashMap<>();
    }

    public static class StatsControllerHolder {
        public static final StatsController instance = new StatsController();
    }

    public static StatsController getInstance() {
        return StatsControllerHolder.instance;
    }

    public Stats getTodayStats() {
        if (!this.dateToStats.containsKey(LocalDate.now())) {
            this.dateToStats.put(LocalDate.now(), new Stats());
        }
        return this.dateToStats.get(LocalDate.now());
    }

    public void addUser(User u) {
        getTodayStats().addUser(u);
    }

    public void addGuest(String guestAddr) {
        getTodayStats().addGuest(guestAddr);
    }

    public void removeGuest(String guestAddr) {
        getTodayStats().removeGuest(guestAddr);
    }

    public List<Map.Entry<LocalDate, Stats>> getAllStats() {
        return this.dateToStats.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
    }

    public void clearAll() {
        this.dateToStats = new HashMap<>();
    }

    public static void main(String[] args) {
        LocalDate ld = LocalDate.now();
        System.out.println(ld);
        LocalDate ld1 = LocalDate.now();

        Map m = new HashMap<>();
        m.put(ld, 1337);

        System.out.println(m.get(ld));
        System.out.println(m.get(ld1));
    }
}
