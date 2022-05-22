package ServiceLayer.DTOs;

import DomainLayer.Stores.Permission;

public class PermissionDTO {
    public String givenBy;
    public byte permissionMask;

    public PermissionDTO(Permission permission) {
        this.permissionMask = permission.getPermissionsMask();
        this.givenBy = permission.getGivenBy();
    }

    public PermissionDTO() {

    }
}

