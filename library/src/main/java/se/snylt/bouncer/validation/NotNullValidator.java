package se.snylt.bouncer.validation;

import se.snylt.bouncer.Param;

public class NotNullValidator extends Validator {

    public NotNullValidator(Param param, String description) {
        super(param, description);
    }

    @Override
    protected Validation checkValid(Param param) {
        if(param.getValue() == null) {
            return Validation.invalid(this);
        }
        return Validation.valid();
    }
}
