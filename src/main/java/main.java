import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;
import Logger.LogUtility;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        SystemImplementor s = new SystemImplementor();
        Response<User> r = s.login("name", "pass");
        s.login("name", "pass");
    }
}
