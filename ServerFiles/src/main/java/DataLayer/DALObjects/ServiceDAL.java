package DataLayer.DALObjects;

import DomainLayer.Stores.Category;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Services")
public class ServiceDAL
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private int service_type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getServiceType() {
        return service_type;
    }

    public void setServiceType(int service_type) {
        this.service_type = service_type;
    }
}
