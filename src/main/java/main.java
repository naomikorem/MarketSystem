import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        SystemImplementor s = new SystemImplementor();
        System.out.println(s.register("name", "pass", "email@gmail.com").hadError());
        Response<User> r = s.login("name", "pass");
        s.login("name", "pass");
    }
}
