package ServiceLayer.DTOs;

public class SupplyParamsDTO {
    public final String ServiceName;
    public final String name;
    public final String address;
    public final String city;
    public final String country;
    public final String zip;

    public SupplyParamsDTO(String ServiceName, String name, String address, String city, String country, String zip)
    {
        this.ServiceName = ServiceName;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zip = zip;
    }
}
