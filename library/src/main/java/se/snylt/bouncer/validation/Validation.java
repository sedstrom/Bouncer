package se.snylt.bouncer.validation;

public class Validation {

    private final boolean isValid;

    private final String invalidReason;

    private Validation(boolean isValid, String invalidReason) {
        this.isValid = isValid;
        this.invalidReason = invalidReason;
    }

    public boolean isValid() {
        return isValid;
    }

    public String invalidReason() {
        return invalidReason;
    }

    public static Validation invalid(Validator validator) {
        return new Validation(false, validator.getDescription());
    }

    public static Validation valid() {
        return new Validation(true, null);
    }

}
