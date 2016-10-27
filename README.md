# Bouncer
Share parameter validation between method and caller.
![](http://i.giphy.com/OdyGA2spBFRCg.gif)

## Disclaimer
This library is in a very early stage and will probably change a lot in the nearest future :)

## Usage

Provide methods through bouncer
```java

    // Actual method is private
    private Void register(RegistrationParams params) {
        params.getListener().onRegistrationSuccessful(params);
        return null;
    }
   
   // Provide through bouncer
   public Bouncer<RegistrationParams, Void> register() {
        return new Bouncer<RegistrationParams, Void>() {
            @Override
            protected Void welcome(RegistrationParams params) {
                return register(params);
            }
        };
    }
```

Define paramter validation:

```java
public class LoginParams extends Params {
    
    ...

     public RegistrationParams(Param<String> username, Param<String> password, Param<RegistrationListener> listener) {
        super();
        
         addValidator(new EmailAddressValidator(username, "Username needs to be an email address."));
        
        ...
    }
}
```

Pass validation before getting access to actual method
```java
  RegistrationParams params = createRegistrationParams(username.text().toString(), password.text().toString());
  CheckResult<Void> result = api.register().check(params);
  if(result.isOk()) {
    result.call(); // Calls encapsulated method
  } else {
    // Buhuu..
  }
```

Get validation result per paramter
```java
 private RegistrationParams createRegistrationParams(String username, String password) {
        return new RegistrationParams(
                new Param<String>(username) {

                    @Override
                    public void onValidationPassed() {
                        usernameTitle.setText("");
                        usernameTitle.setTextColor(Color.GREEN);
                    }

                    @Override
                    public void onValidationFailed(String reason) {
                        usernameTitle.setText(reason);
                        usernameTitle.setTextColor(Color.RED);
                    }
                },
                ...
        );
    }
```
