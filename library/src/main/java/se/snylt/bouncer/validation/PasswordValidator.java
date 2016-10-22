package se.snylt.bouncer.validation;

import se.snylt.bouncer.Param;

public class PasswordValidator extends Validator<String> {

    public PasswordValidator(Param<String> param, String description) {
        super(param, description);
    }

    @Override
    protected Validation checkValid(Param<String> param) {
        if(param.getValue() == null) {
            return Validation.invalid(this);
        }

        if(param.getValue().length() < 6) {
            return Validation.invalid(this);
        }

        return Validation.valid();
    }
}
