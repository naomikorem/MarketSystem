package DataLayer;

import DataLayer.DALObjects.PermissionDAL;

public class PermissionManager extends DALManager<PermissionDAL, Integer> {

    public PermissionManager() {
        super(PermissionDAL.class);
    }
}
