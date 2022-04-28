package DomainLayer.Stores;

public class Permission {

    public enum PermissionEnum {
        canAssignManager(0x1),
        canChangeItems(0x2),
        canChangeDiscount(0x4),
        canChangePurchase(0x8);

        PermissionEnum(int flag) {
            this.flag = (byte) flag;
        }
        private byte flag;
    }

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

    public String getGivenBy() {
        return this.givenBy;
    }
}
