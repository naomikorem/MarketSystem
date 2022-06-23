package Utility;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class Utility {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isHashed(String password) {
        return password.substring(0, 3).equals("$2a");
    }

}
