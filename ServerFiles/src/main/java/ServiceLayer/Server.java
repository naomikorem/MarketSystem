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

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class })
public class Server {
    public static void main(String[] args) {
        LogUtility.info("Starting to load stores...");
        StoreController.getInstance();
        LogUtility.info("Finished to load stores");
        LogUtility.info("Starting to load notifications and services...");
        MarketManagementFacade.getInstance();
        LogUtility.info("Finished to load notifications and services");
        LogUtility.info("Starting to load system admins...");
        AdminController.getInstance();
        SpringApplication.run(Server.class, args);
    }
}
