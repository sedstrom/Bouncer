# Bouncer
Share parameter validation between methods and callers.

## Disclaimer
This library is in a very early stage and will probably change a lot in the nearest future :)

## Usage

Provide methods through bouncer
```java

    // Actual method is private
    private Void login(LoginParams params) {
        
        params.listener.getValue().onLoginSuccessful();

        return null;
    }
   
   // Provide through bouncer
   public Bouncer<LoginParams, Void> login() {
        return new Bouncer<LoginParams, Void>() {
            @Override
            protected Void welcome(LoginParams params) {
                return login(params);
            }
        };
    }
```

Define paramter validation:

```java
public class LoginParams extends Params {
    
    ...

    public LoginParams(Param<String> username, Param<String> password, Param<LoginListener> listener) {
        super();
        
        registerValidator(new EmailAddressValidator(this.username, "Username needs to be an email address."));
        
        ...
    }
}
```

Pass validation before getting access to actual method
```java
  LoginParams params = createLoginParams(username.text().toString(), password.text().toString());
  CheckResult<Void> result = api.login().check(params);
  if(result.isOk()) {
    result.call(); // Calls encapsulated method
  } else {
    // Buhuu..
  }
```

Get validation result per paramter
```java
private LoginParams createLoginParams(String username, String password) {
        return new LoginParams(
                new Param<String>(username) {

                    @Override
                    public void onValidationPassed() {
                        usernameTitle.setText("Username is valid!");
                    }

                    @Override
                    public void onValidationFailed(String reason) {
                        usernameTitle.setText(reason);
                    }
                },
                ...
        );
    }
```
![](http://i.giphy.com/OdyGA2spBFRCg.gif)
