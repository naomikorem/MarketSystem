package Exceptions;

import Logger.LogUtility;

public class LogException extends RuntimeException {
    public LogException(String name, String message) {
        super(name);
        LogUtility.error(message);
    }
}
