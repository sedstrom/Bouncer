package se.snylt.bouncer;

/**
 * Immutable parameter container
 */
public class Param<V> {

    private final V value;

    public Param(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    /**
     * Params did not pass validation.
     * @param reason arbitrary description of why the validation did not pass.
     */
    public void onValidationFailed(String reason){}

    /**
     * Params did pass the validation
     */
    public void onValidationPassed(){}
}
