package se.snylt.bouncer.validation;

import java.util.regex.Pattern;

import se.snylt.bouncer.Param;

public class RegexValidator extends Validator<String> {

    private final Pattern pattern;

    public RegexValidator(Param<String> param, Pattern pattern, String description) {
        super(param, description);
        this.pattern = pattern;
    }

    @Override
    protected Validation checkValid(Param<String> param) {
        if(!pattern.matcher(param.getValue()).find()) {
            return Validation.invalid(this);
        }
        return Validation.valid();
    }
}
