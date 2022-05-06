package DomainLayer.SystemManagement.ExternalServices;

import Utility.LogUtility;

import java.rmi.ConnectException;

public abstract class AbstractProxy
{
    public static final String GOOD_STUB_NAME = "good stub";
    public static final String GOOD_STUB_NAME_2 = "good stub 2";
    public static final String BAD_STUB_NAME = "bad stub";
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
