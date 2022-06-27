package ServiceLayer;

import DomainLayer.Stores.StoreController;
import DomainLayer.SystemManagement.ExternalServices.AbstractProxyController;
import DomainLayer.SystemManagement.ExternalServices.ExternalServicesHandler;
import DomainLayer.SystemManagement.ExternalServices.SupplyServices.SupplyProxyController;
import DomainLayer.SystemManagement.MarketManagementFacade;
import DomainLayer.SystemManagement.NotificationManager.NotificationController;
import DomainLayer.Users.AdminController;
import Utility.LogUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class })
public class Server {

    private static final String CONFIG_PATH = "config.properties";
    public static final Properties prop = new Properties();

    static {
        loadConfig();
    }
    public static void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LogUtility.info("Starting to load stores...");
        StoreController.getInstance();
        LogUtility.info("Finished to load stores");
        LogUtility.info("Starting to load notifications and services...");
        MarketManagementFacade.getInstance();
        LogUtility.info("Finished to load notifications and services");
        LogUtility.info("Starting to load system admins...");
        AdminController.getInstance();
        LogUtility.info("Finished to load System admins");
        SpringApplication.run(Server.class, args);
    }
}
