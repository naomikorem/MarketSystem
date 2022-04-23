package Utility;

import org.apache.commons.validator.routines.EmailValidator;

public class Utility {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
