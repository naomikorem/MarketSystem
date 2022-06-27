package DomainLayer.SystemManagement.ExternalServices;

import ServiceLayer.Server;
import Utility.LogUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.rmi.ConnectException;

public abstract class AbstractProxy
{
    public static final String GOOD_STUB_NAME = "good stub";
    public static final String GOOD_STUB_NAME_2 = "good stub 2";
    public static final String BAD_STUB_NAME = "bad stub";
    public static final String WSEP_PAYMENT = "wsep payment";
    public static final String WSEP_PAYMENT_URL = Server.prop.getProperty("paymentURL");
    public static final String WSEP_SUPPLY = "wsep supply";
    public static final String WSEP_SUPPLY_URL = Server.prop.getProperty("supplyURL");

    protected String name;
    protected String url;
    protected final RestTemplate restTemplate;
    protected final HttpHeaders headers;

    public AbstractProxy(String name, String url)
    {
        this.name = name;
        this.url = url;
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public boolean connect() throws ConnectException
    {
        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not connect to bad stub service");
            throw new ConnectException("Could not connect to bad stub service");
        }

        HttpClientPost.handshake(this.name, this.url, this.headers, this.restTemplate);
        LogUtility.info("connected to external service named: " + this.name + ", url:" + this.url);
        return true;
    }
}
