package se.snylt.bouncer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.snylt.bouncer.validation.Validator;

public abstract class Params {

    private final List<Validator> validators = new ArrayList<>();

    public Params(Validator... validators) {
        this.validators.addAll(Arrays.asList(validators));
    }

    protected void addValidator(Validator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        boolean allValid = true;
        for(Validator validator : validators) {
            allValid = (validator.validate().isValid() && allValid);
        }
        return allValid;
    }

    public Validator getValidator(int index) {
        return validators.get(index);
    }
}
