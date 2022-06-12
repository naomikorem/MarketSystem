import DataLayer.DALObjects.StoreDAL;
import DataLayer.StoreManager;
import DomainLayer.Response;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {


            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(prop);
    }
}
