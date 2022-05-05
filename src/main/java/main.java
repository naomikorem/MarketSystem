import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        SystemImplementor s = new SystemImplementor();
        Response<User> r1 = s.register("name", "pass", "email@gmail.com");
        System.out.println(r1.hadError());
        User u = r1.getObject();
        Response<User> r = s.login(u.getName(), "pass");
        s.login(u.getName(), "pass");
    }
}
