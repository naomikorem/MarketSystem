package Exceptions;

import Utility.LogUtility;

public class LogException extends RuntimeException {
    public LogException(String name, String message) {
        super(name);
        LogUtility.info(message);
    }
}
