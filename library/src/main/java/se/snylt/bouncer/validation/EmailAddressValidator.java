package se.snylt.bouncer.validation;

import java.util.regex.Pattern;

import se.snylt.bouncer.Param;

public class EmailAddressValidator extends RegexValidator {

    private final static String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public EmailAddressValidator(Param<String> param, String description) {
        super(param, Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE), description);
    }
}
