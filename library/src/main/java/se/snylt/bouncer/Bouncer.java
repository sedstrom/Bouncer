package se.snylt.bouncer;

public abstract class Bouncer<P extends Params, MethodResult> {

    /**
     * Check if params are valid
     * @param params params to validate
     * @return true if valid
     */
    public CheckResult<MethodResult> check(P params) {
        if(params.isValid()) {
            return new CheckResult(welcomeMethodCall(params));
        } else {
            return new CheckResult(null);
        }
    }

    /**
     * Wrap welcome method in call object
     * @param params valid params
     * @return MethodCall with encapsulated entry point to real method
     */
    private MethodCall<MethodResult> welcomeMethodCall(final P params) {
        return new MethodCall<MethodResult>() {

            @Override
            public MethodResult call() {
                return welcome(params);
            }
        };
    }

    /**
     * Params are valid and can be used without any further validations
     * @param params valid params
     */
    protected abstract MethodResult welcome(P params);
}
