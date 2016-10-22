package se.snylt.bouncer.android;

import se.snylt.bouncer.Param;
import se.snylt.bouncer.Params;
import se.snylt.bouncer.validation.EmailAddressValidator;
import se.snylt.bouncer.validation.NotNullValidator;
import se.snylt.bouncer.validation.PasswordValidator;

public class LoginParams extends Params {

    public final Param<String> username;

    public final Param<String> password;

    public final Param<LoginListener> listener;

    public LoginParams(Param<String> username, Param<String> password, Param<LoginListener> listener) {
        super();
        this.username = username;
        registerValidator(new EmailAddressValidator(this.username, "Username needs to be an email address."));

        this.password = password;
        registerValidator(new PasswordValidator(this.password, "Password needs to be at least 6 characters."));

        this.listener = listener;
        registerValidator(new NotNullValidator(this.listener, "Listener can't be null."));
    }
}
