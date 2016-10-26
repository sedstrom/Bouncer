package se.snylt.bouncer.validation;

import se.snylt.bouncer.Param;

class CombinedValidator<T> extends Validator<T> {

    private final Validator<T>[] validators;

    protected CombinedValidator(Param<T> param, Validator<T> ...validators) {
        super(param, CombinedValidator.class + " must have inner validators.");
        this.validators = validators;
    }

    @Override
    protected Validation checkValid(Param<T> param) {
        if(validators.length == 0) {
            return Validation.invalid(this);
        }

        for(Validator validator : validators) {
            Validation validation = validator.checkValid(param);
            if(!validation.isValid()) {
                return validation;
            }
        }

        return Validation.valid();
    }

    @Override
    protected String getDescription() {
        StringBuilder builder = new StringBuilder();
        for(Validator validator : validators) {
            builder.append(validator.getDescription());
            builder.append("\n");
        }
        return builder.toString();
    }
}
