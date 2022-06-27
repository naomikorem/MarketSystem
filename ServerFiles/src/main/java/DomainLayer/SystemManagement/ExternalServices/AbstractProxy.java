package DomainLayer.SystemManagement.ExternalServices;

import ServiceLayer.Server;
import Utility.LogUtility;

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

    public AbstractProxy(String name)
    {
        this.name = name;
        this.url = "";
    }

    public boolean connect(String url) throws ConnectException
    {
        this.url = url;

        // TODO: add connection to real service

        if (this.name.equals(GOOD_STUB_NAME) || this.name.equals(GOOD_STUB_NAME_2))
        {
            return true;
        }
        else if (this.name.equals(BAD_STUB_NAME))
        {
            LogUtility.error("Could not connect to bad stub service");
            throw new ConnectException("Could not connect to bad stub service");
        }

        LogUtility.error("Could not connect to external service named: " + this.name);
        throw new ConnectException("Could not connect to external service named: " + this.name);
    }
}
