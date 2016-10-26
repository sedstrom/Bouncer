package se.snylt.bouncer.validation;

import se.snylt.bouncer.Param;

public abstract class Validator<T> {

    private final se.snylt.bouncer.Param<T> param;

    private final String description;

    protected Validator(se.snylt.bouncer.Param<T> param, String description) {
        this.param = param;
        this.description = description;
    }

    public Param<T> getParam(){
        return param;
    }

    public Validation validate() {
        Validation validation = checkValid(param);
        if(!validation.isValid()) {
            param.onValidationFailed(validation.invalidReason());
        } else {
            param.onValidationPassed();
        }
        return validation;
    }

    protected abstract Validation checkValid(se.snylt.bouncer.Param<T> param);

    protected String getDescription() {
        return description;
    }

    public static <T> Validator<T> combine(Param<T> param, Validator<T> ...validators) {
        return new CombinedValidator<>(param, validators);
    }
}
