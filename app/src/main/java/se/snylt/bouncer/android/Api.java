package se.snylt.bouncer.android;

import se.snylt.bouncer.Bouncer;

public class Api {

    public Bouncer<LoginParams, Void> login() {
        return new Bouncer<LoginParams, Void>() {
            @Override
            protected Void welcome(LoginParams params) {
                return login(params);
            }
        };
    }

    private Void login(LoginParams params) {

        // Success!
        params.listener.getValue().onLoginSuccessful();

        return null;
    }
}
