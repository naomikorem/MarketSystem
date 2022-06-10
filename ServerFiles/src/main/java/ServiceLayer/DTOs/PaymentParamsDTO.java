package ServiceLayer.DTOs;

public class PaymentParamsDTO {
    public final String ServiceName;
    public final String card_number;
    public final String month;
    public final String year;
    public final String holder;
    public final String ccv;
    public final String id;

    public PaymentParamsDTO(String ServiceName, String card_number, String month, String year, String holder, String ccv, String id)
    {
        this.ServiceName = ServiceName;
        this.card_number = card_number;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.ccv = ccv;
        this.id = id;
    }
}