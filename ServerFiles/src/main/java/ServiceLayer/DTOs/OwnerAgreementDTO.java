package ServiceLayer.DTOs;

import DomainLayer.Stores.OwnerAgreement;
import DomainLayer.Users.User;

import java.util.ArrayList;
import java.util.List;

public class OwnerAgreementDTO {

    public List<String> notApprovedOwners;
    public int store;
    public String owner;
    public Boolean isApproved;
    public String givenBy;

    public OwnerAgreementDTO(OwnerAgreement oa) {
        this.notApprovedOwners = new ArrayList<>(oa.getNotApprovedOwners());
        this.owner = oa.getOwner().getName();
        this.isApproved = oa.getApproved();
        this.givenBy = oa.getGivenBy();
    }

}
