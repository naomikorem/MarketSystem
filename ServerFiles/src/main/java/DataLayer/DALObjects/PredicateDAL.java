package DataLayer.DALObjects;

import DataLayer.DALObject;

import javax.persistence.*;

@Entity
@Table(name = "Predicates")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PredicateDAL implements DALObject<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
