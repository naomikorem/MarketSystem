package DomainLayer.Stores;

import DomainLayer.Users.User;

import java.util.LinkedList;
import java.util.List;

public class OwnerAgreement {
    List<String> notApprovedOwners;
    int store;
    User owner;
    Boolean isApproved;
    String givenBy;

    public OwnerAgreement(int store, User owner, String givenBy, List<String> notApprovedOwners) {
        this.notApprovedOwners = notApprovedOwners;
        this.store = store;
        this.owner = owner;
        this.isApproved = false;
        this.givenBy = givenBy;
    }

    public User getOwner() {
        return owner;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void approve(String userName) {
        notApprovedOwners.remove(userName);
        if(notApprovedOwners.isEmpty())
            isApproved = true;
    }

    public List<String> getNotApprovedOwners() {
        return notApprovedOwners;
    }

    public int getStore() {
        return store;
    }
}
