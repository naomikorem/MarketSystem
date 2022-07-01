package Utility;

import ServiceLayer.Server;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LogUtility {
    private static final Map<String, Logger> loggersMap = new HashMap<>();
    private static final String CONFIG_PATH = "src/main/resources/log4j.properties";
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

        Properties p = prop;
        p.setProperty("log4j.appender.errorLog.File", Server.prop.getProperty("errorLog"));
        p.setProperty("log4j.appender.eventLog.File", Server.prop.getProperty("eventLog"));
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(p);
    }

    private static StackTraceElement getPreviousStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            String name = stackTrace[i].getClassName();
            if (!name.equals(LogUtility.class.getName())) {
                return stackTrace[i];
            }
        }
        return null;
    }

    private static String getCallingClassName() {
        StackTraceElement stackTrace = getPreviousStackTrace();
        if (stackTrace != null) {
            return stackTrace.getClassName();
        }
        return null;
    }

    private static Logger getLogger() {
        String className = getCallingClassName();
        if (!loggersMap.containsKey(className)) {
            loggersMap.put(className, Logger.getLogger(className));
        }
        return loggersMap.get(className);
    }

    private static String addLineNumber(String message) {
        StackTraceElement stackTraceElement = getPreviousStackTrace();
        if (stackTraceElement != null) {
            return String.format("%s - %s", getPreviousStackTrace().getLineNumber(), message);
        }
        return message;
    }

    public static void info(String message) {
        getLogger().info(addLineNumber(message));
    }

    public static void debug(String message) {
        getLogger().debug(addLineNumber(message));
    }

    public static void warn(String message) {
        getLogger().warn(addLineNumber(message));
    }

    public static void error(String message) {
        getLogger().error(addLineNumber(message));
    }
}
