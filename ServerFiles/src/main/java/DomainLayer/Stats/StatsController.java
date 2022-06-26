package DomainLayer.Stats;

import DataLayer.DALObjects.StatisticsDAL;
import DataLayer.StatisticsManager;
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
        StatisticsManager.getInstance().getAllObjects().forEach((s) -> {
            dateToStats.put(s.getDate(), s.toDomain());
        });
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
            saveToday();
        }
        return this.dateToStats.get(LocalDate.now());
    }

    public void saveToday() {
        StatisticsManager.getInstance().addObject(getTodayStats().toDAL(LocalDate.now()));
    }

    public void addUser(User u) {
        getTodayStats().addUser(u);
        saveToday();
    }

    public void addGuest(String guestAddr) {
        getTodayStats().addGuest(guestAddr);
        saveToday();
    }

    public void removeGuest(String guestAddr) {
        getTodayStats().removeGuest(guestAddr);
        saveToday();
    }

    public List<Map.Entry<LocalDate, Stats>> getAllStats() {
        return this.dateToStats.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
    }

    public void clearAll() {
        this.dateToStats = new HashMap<>();
        StatisticsManager.getInstance().clearTable();
    }
}
