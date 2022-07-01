package DataLayer;

import DataLayer.DALObjects.PermissionDAL;

public class PermissionManager extends DALManager<PermissionDAL, Integer> {

    private static class PermissionManagerHolder {
        static final PermissionManager instance = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return PermissionManager.PermissionManagerHolder.instance;
    }

    public PermissionManager() {
        super(PermissionDAL.class);
    }
}
