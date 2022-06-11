import DataLayer.DALObjects.StoreDAL;
import DataLayer.StoreManager;
import DomainLayer.Response;
import DomainLayer.Stores.Predicates.SimplePredicate;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        List<StoreDAL> l = StoreManager.getInstance().getAllStores();
        System.out.println(l);
    }
}
