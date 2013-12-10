package es.udc.ws.util.validation;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.udc.ws.util.exceptions.InputValidationException;

public final class PropertyValidator {

    private PropertyValidator() {}

    public static void validateLong(String propertyName,
            long value, int lowerValidLimit, int upperValidLimit)
            throws InputValidationException {

        if ( (value < lowerValidLimit) || (value > upperValidLimit) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " + value);
        }

    }

    public static void validateNotNegativeLong(String propertyName,
            long longValue) throws InputValidationException {

        if (longValue < 0) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0): " +    longValue);
        }

    }

    public static void validateDouble(String propertyName,
            double doubleValue, double lowerValidLimit, double upperValidLimit)
            throws InputValidationException {

        if ((doubleValue < lowerValidLimit) ||
                (doubleValue > upperValidLimit)) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be gtrater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " +
                    doubleValue);
        }

    }

    public static void validateMandatoryString(String propertyName,
            String stringValue) throws InputValidationException {

        if ( (stringValue == null) || (stringValue.length() == 0) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it cannot be null neither empty): " +
                    stringValue);
        }

    }

    public static void validatePastDate(String propertyName,
            Calendar propertyValue) throws InputValidationException {

        Calendar now = Calendar.getInstance();
        if ( (propertyValue == null) || (propertyValue.after(now)) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be a past date): " +
                    propertyValue);
        }
    }
    
    public static void validateDate(String propertyName,
            Calendar propertyValue) throws InputValidationException {

        if (propertyValue == null) {
            throw new InputValidationException("Invalid " + propertyName + " value: " + propertyValue);
        }
    }

    public static void validateCreditCard(String propertyValue)
            throws InputValidationException {

        boolean validCreditCard = true;
        if ( (propertyValue != null) && (propertyValue.length() == 16) ) {
            try {
                new BigInteger(propertyValue);
            } catch (NumberFormatException e) {
                validCreditCard = false;
            }
        } else {
            validCreditCard = false;
        }
        if (!validCreditCard) {
            throw new InputValidationException("Invalid credit card number" +
                    " (it should be a sequence of 16 numeric digits): " +
                    propertyValue);
        }

    }
    
    public static void validateEmail(String propertyValue) throws InputValidationException {
        Pattern pattern;
        Matcher matcher;
        
        if (propertyValue == null) {
            throw new InputValidationException("Invalid email");
        }
     
        String EMAIL_PATTERN = 
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
     
        pattern = Pattern.compile(EMAIL_PATTERN);
     
        matcher = pattern.matcher(propertyValue);
        if (!matcher.matches()) {
            throw new InputValidationException("Invalid email");
        }
    }

}
