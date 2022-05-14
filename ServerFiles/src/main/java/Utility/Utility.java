package Utility;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class Utility {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

}
