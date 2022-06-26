package DataLayer;

import DataLayer.DALObjects.StatisticsDAL;
import DataLayer.DALObjects.UserDAL;

import java.time.LocalDate;

public class StatisticsManager extends DALManager<StatisticsDAL, LocalDate> {

    private static class UserManagerHolder {
        static final StatisticsManager instance = new StatisticsManager();
    }

    public static StatisticsManager getInstance() {
        return UserManagerHolder.instance;
    }

    public StatisticsManager() {
        super(StatisticsDAL.class);
    }
}
