package DataLayer.DALObjects;

import DataLayer.DALObject;
import DomainLayer.Stores.PurchasePolicy.AbstractPurchasePolicy;

import javax.persistence.*;

@Entity
@Table(name = "Policy")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PurchasePolicyDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract AbstractPurchasePolicy toDomain();
}
