package se.snylt.bouncer.android.api;

import se.snylt.bouncer.Bouncer;

public class Api {

    public Bouncer<RegistrationParams, Void> register() {
        return new Bouncer<RegistrationParams, Void>() {
            @Override
            protected Void welcome(RegistrationParams params) {
                return register(params);
            }
        };
    }

    private Void register(RegistrationParams params) {
        params.getListener().onRegistrationSuccessful(params);
        return null;
    }
}
