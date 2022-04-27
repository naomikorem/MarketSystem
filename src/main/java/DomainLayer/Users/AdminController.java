package DomainLayer.Users;

import DomainLayer.Stores.StoreController;
import Utility.LogUtility;

import java.util.HashSet;
import java.util.Set;

public class AdminController {

    private Set<String> admins;

    private AdminController() {
        this.admins = new HashSet<>();

        // load database

        if (admins.isEmpty()) {
            admins.add(UserController.DEFAULT_ADMIN_EMAIL);
        }

    }

    public boolean addAdmin(String name) {
        synchronized (admins) {
            if (admins.contains(name)) {
                return false;
            }
            admins.add(name);
            LogUtility.info(String.format("Admin %s was added", name));
            return true;
        }
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
            LogUtility.info(String.format("Admin %s was removed", name));
            return false;
        }
    }

    private static class AdminControllerHolder {
        static final AdminController instance = new AdminController();
    }

    public static AdminController getInstance() {
        return AdminControllerHolder.instance;
    }
}
