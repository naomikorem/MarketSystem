package ServiceLayer.DTOs;

import DomainLayer.Stats.Stats;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class StatsDTO {
    public LocalDate date;
    public int guests;
    public int regulars;
    public int managers;
    public int owners;
    public int admins;

    public StatsDTO(LocalDate date, Stats stats) {
        this.date = date;
        this.guests = (int) stats.guestsCount();
        this.regulars = (int) stats.regularUsersCount();
        this.managers = (int) stats.managerUsersCount();
        this.owners = (int) stats.ownerUsersCount();
        this.admins = (int) stats.adminUsersCount();
    }
}
