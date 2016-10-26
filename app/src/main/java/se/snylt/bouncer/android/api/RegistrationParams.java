package se.snylt.bouncer.android.api;

import java.util.regex.Pattern;

import se.snylt.bouncer.Param;
import se.snylt.bouncer.Params;
import se.snylt.bouncer.validation.EmailAddressValidator;
import se.snylt.bouncer.validation.NotNullValidator;
import se.snylt.bouncer.validation.RegexValidator;
import se.snylt.bouncer.validation.Validator;

public class RegistrationParams extends Params {

    public RegistrationParams(Param<String> username, Param<String> password, Param<RegistrationListener> listener) {
        super();
        // TODO use key-value instead of index.

        // 0. Username
        addValidator(new EmailAddressValidator(username, "Username needs to be an email address."));

        // 1. Password
        addValidator(
            Validator.combine(password,
                        new RegexValidator(password,
                                Pattern.compile("^.{8,}$"),
                                "Password needs to have at least 8 characters"),
                        new RegexValidator(password,
                                Pattern.compile(".*[a-z].*"),
                                "Password must have one lower case letter"),
                        new RegexValidator(password,
                                Pattern.compile(".*\\d+.*"),
                                "Password needs to contain at lest one digit"),
                        new RegexValidator(password,
                                Pattern.compile(".*[A-Z].*"),
                                "Password must have one upper case letter")));

        // 2. Listener
        addValidator(new NotNullValidator(listener, "Listener can't be null."));
    }

    public String getUsername() {
        return (String) getValidator(0).getParam().getValue();
    }

    public String getPassword() {
        return (String) getValidator(1).getParam().getValue();
    }

    public RegistrationListener getListener() {
        return (RegistrationListener) getValidator(2).getParam().getValue();
    }

}
