package se.snylt.bouncer;

public class CheckResult<T> {

    private final MethodCall<T> methodCall;

    public CheckResult(MethodCall<T> methodCall) {
        this.methodCall = methodCall;
    }

    public boolean isOk(){
        return methodCall != null;
    }

    public T call() {
        return methodCall.call();
    }
}
