package DomainLayer.Users;

import DataLayer.AdminManager;
import DataLayer.DALObjects.AdminDAL;
import DataLayer.StoreManager;
import DomainLayer.Stores.StoreController;
import Utility.LogUtility;

import java.util.HashSet;
import java.util.Set;

public class AdminController {

    private Set<String> admins;

    private AdminController() {
        this.admins = new HashSet<>();

        // load database
        AdminManager.getInstance().loadAllSystemAdmins().forEach(a -> admins.add(a.getAdmin_name()));


        if (admins.isEmpty()) {
            addAdmin(UserController.DEFAULT_ADMIN_USER);
        }

    }

    public void clearAll() {
        admins = new HashSet<>();
        AdminManager.getInstance().clearTable();

        if (admins.isEmpty()) {
            addAdmin(UserController.DEFAULT_ADMIN_USER);
        }
    }

    public boolean addAdmin(String name) {
        synchronized (admins) {
            if (admins.contains(name)) {
                return false;
            }
            AdminManager.getInstance().addObject(toDAL(name));
            LogUtility.info(String.format("Admin %s was added", name));
            admins.add(name);
            return true;
        }
    }

    private AdminDAL toDAL(String name)
    {
        AdminDAL admin = new AdminDAL();
        admin.setAdmin_name(name);
        return admin;
    }

    public boolean isAdmin(String name) {
        synchronized (admins) {
            return admins.contains(name);
        }
    }

    public boolean removeAdmin(String name) {
        synchronized (admins) {
            if (admins.size() <= 1) {
                return false;
            }
            if (admins.contains(name)) {
                admins.remove(name);
                return true;
            }
            AdminManager.getInstance().removeObject(toDAL(name));
            LogUtility.info(String.format("Admin %s was removed", name));
            return false;
        }
    }

    public boolean hasAdmin()
    {
        synchronized (admins) {
            return !this.admins.isEmpty();
        }
    }

    private static class AdminControllerHolder {
        static final AdminController instance = new AdminController();
    }

    public static AdminController getInstance() {
        return AdminControllerHolder.instance;
    }
}
