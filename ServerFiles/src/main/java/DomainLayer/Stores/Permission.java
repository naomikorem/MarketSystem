package DomainLayer.Stores;

import DataLayer.DALObjects.PermissionDAL;
import DomainLayer.Users.User;

public class Permission {

    public enum PermissionEnum {
        canAssignManager(0x1),
        canChangeItems(0x2),
        canChangeDiscount(0x4),
        canChangePurchase(0x8),
        canProcessBids(0x10);
        PermissionEnum(int flag) {
            this.flag = (byte) flag;
        }
        public byte flag;
    }
    private int id;
    private String givenBy;
    private byte permissionsMask;
    public Permission(String givenBy) {
        this.givenBy = givenBy;
    }

    public void setPermissionsMask(byte mask) {
        this.permissionsMask = mask;
    }

    public boolean canAssignManager() {
        return (permissionsMask & PermissionEnum.canAssignManager.flag) != 0;
    }

    public boolean canChangeItems() {
        return (permissionsMask & PermissionEnum.canChangeItems.flag) != 0;
    }

    public boolean canChangeDiscount() {
        return (permissionsMask & PermissionEnum.canChangeDiscount.flag) != 0;
    }

    public boolean canChangePurchase() {
        return (permissionsMask & PermissionEnum.canChangePurchase.flag) != 0;
    }
    public boolean canProcessBids() {
        return (permissionsMask & PermissionEnum.canProcessBids.flag) != 0;
    }

    public String getGivenBy() {
        return this.givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public byte getPermissionsMask() {
        return permissionsMask;
    }

    public PermissionDAL toDAL(User u) {
        PermissionDAL res = new PermissionDAL();
        res.setId(this.id);
        res.setPermissionMask(getPermissionsMask());
        res.setGivenBy(getGivenBy());
        res.setManager(u.toDAL());
        return res;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
