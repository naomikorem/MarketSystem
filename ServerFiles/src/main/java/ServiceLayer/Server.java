package ServiceLayer;

import DataLayer.DALManager;
import DataLayer.DALObjects.UserDAL;
import DataLayer.UserManager;
import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxyController;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.AdminController;
import DomainLayer.Users.UserController;
import ServiceLayer.ParseFile.Parser;
import Utility.LogUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class })
public class Server {

    private static final String CONFIG_PATH = "config.properties";
    public static final Properties prop = new Properties();

    static {
        loadConfig();
    }
    public static boolean useDB = Boolean.parseBoolean(Server.prop.getProperty("useDatabase", "false"));
    public static final String INIT_FILE_PATH = prop.getProperty("initFile");
    private static Parser parser;

    public static void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<UserDAL> users_in_system = UserManager.getInstance().getAllUsers();
        parser = new Parser();
        if(users_in_system.size() == 1 && users_in_system.get(0).getUserName().equals(UserController.DEFAULT_ADMIN_USER))
        {
            LogUtility.info("Starting to load from initialization file...");
            // load from file
            parser.runCommands();
            LogUtility.info("Finished to load from initialization file...");
        }
        else
        {
            LogUtility.info("Starting to load stores...");
            StoreController.getInstance();
            LogUtility.info("Finished to load stores");
            LogUtility.info("Starting to load notifications and services...");
            MarketManagementFacade.getInstance();
            LogUtility.info("Finished to load notifications and services");
            LogUtility.info("Starting to load system admins...");
            AdminController.getInstance();
            LogUtility.info("Finished to load System admins");
        }

        SpringApplication.run(Server.class, args);
    }
}
