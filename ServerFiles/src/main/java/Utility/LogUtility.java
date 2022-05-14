package Utility;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LogUtility {
    private static final Map<String, Logger> loggersMap = new HashMap<>();

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
