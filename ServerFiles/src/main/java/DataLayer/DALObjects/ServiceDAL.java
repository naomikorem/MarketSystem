package DataLayer.DALObjects;

import DataLayer.DALObject;
import DataLayer.ServiceType;

import javax.persistence.*;

@Entity
@Table(name = "Services")
public class ServiceDAL implements DALObject<Integer>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Url")
    private String url;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Type")
    private ServiceType service_type;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ServiceType getServiceType() {
        return this.service_type;
    }

    public void setServiceType(ServiceType service_type) {
        this.service_type = service_type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
