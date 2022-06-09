package DataLayer.DALObjects;

import DataLayer.ServiceType;

import javax.persistence.*;

@Entity
@Table(name = "Services")
public class ServiceDAL
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Url")
    private String url;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Type")
    private ServiceType service_type;


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

    public ServiceType getServiceType() {
        return service_type;
    }

    public void setServiceType(ServiceType service_type) {
        this.service_type = service_type;
    }
}
