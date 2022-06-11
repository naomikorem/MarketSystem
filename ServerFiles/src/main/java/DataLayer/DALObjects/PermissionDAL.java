package DataLayer.DALObjects;


import DataLayer.DALObject;
import DomainLayer.Stores.Permission;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Permissions")
public class PermissionDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String givenBy;
    private byte permissionMask;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manager")
    private UserDAL manager;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public byte getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(byte permissionMask) {
        this.permissionMask = permissionMask;
    }

    public UserDAL getManager() {
        return manager;
    }

    public void setManager(UserDAL manager) {
        this.manager = manager;
    }

    public Permission toDomain() {
        Permission p = new Permission(getGivenBy());
        p.setPermissionsMask(getPermissionMask());
        return p;
    }
}
