import DomainLayer.Response;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        SimplePredicate sp = new SimplePredicate(12);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sp);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            System.out.println(yourBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}
