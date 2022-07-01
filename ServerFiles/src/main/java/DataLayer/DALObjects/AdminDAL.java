package DataLayer.DALObjects;

import DataLayer.DALObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Admins")
public class AdminDAL implements DALObject<String>
{
    @Id
    private String admin_name;

    public AdminDAL() {}

    @Override
    public String getId() {
        return getAdmin_name();
    }

    @Override
    public void setId(String id) {
        setAdmin_name(id);
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }
}
