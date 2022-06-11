package DataLayer.DALObjects;

import DataLayer.DALObject;
import DomainLayer.Stores.DiscountPolicy.AbstractDiscountPolicy;

import javax.persistence.*;

@Entity
@Table(name = "discount")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DiscountDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract AbstractDiscountPolicy toDomain();
}
