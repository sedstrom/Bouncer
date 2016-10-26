package se.snylt.bouncer.android;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import se.snylt.bouncer.CheckResult;
import se.snylt.bouncer.Param;
import se.snylt.bouncer.android.api.Api;
import se.snylt.bouncer.android.api.RegistrationListener;
import se.snylt.bouncer.android.api.RegistrationParams;


public class MainActivityRx extends AppCompatActivity implements RegistrationListener {

    private final Api api = new Api();

    @BindView(R.id.usernameTitle)
    TextView usernameTitle;

    @BindView(R.id.usernameEt)
    EditText usernameEt;

    @BindView(R.id.passwordTitle)
    TextView passwordTitle;

    @BindView(R.id.passwordEt)
    EditText passwordEt;

    @BindView(R.id.registerBtn)
    Button registerBtn;

    private Subscription login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        login = Observable
                // Create new params each time either of username or password input changes and check if valid
                .combineLatest(
                        RxTextView.textChangeEvents(usernameEt),
                        RxTextView.textChangeEvents(passwordEt),
                        new Func2<TextViewTextChangeEvent, TextViewTextChangeEvent, CheckResult<Void>>() {
                            @Override
                            public CheckResult<Void> call(TextViewTextChangeEvent username, TextViewTextChangeEvent password) {
                                RegistrationParams params = createRegistrationParams(username.text().toString(), password.text().toString());
                                return api.register().check(params);
                            }
                        })
                // Sync button enabled / disabled
                .doOnNext(new Action1<CheckResult<Void>>() {
                    @Override
                    public void call(CheckResult<Void> checkResult) {
                        registerBtn.setEnabled(checkResult.isOk());
                    }
                })
                // No need to let through invalid params
                .filter(new Func1<CheckResult<Void>, Boolean>() {
                    @Override
                    public Boolean call(CheckResult<Void> result) {
                        return result.isOk();
                    }
                })
                .onBackpressureLatest()
                // Button click + latest valid params
                .withLatestFrom(RxView.clicks(registerBtn), new Func2<CheckResult<Void>, Void, CheckResult<Void>>() {
                    @Override
                    public CheckResult<Void> call(CheckResult<Void> result, Void aVoid) {
                        return result;
                    }
                })
                // Call register method
                .doOnNext(new Action1<CheckResult<Void>>() {
                    @Override
                    public void call(CheckResult<Void> result) {
                        result.call();
                    }
                }).subscribe();
    }

    private RegistrationParams createRegistrationParams(String username, String password) {
        return new RegistrationParams(
                new Param<String>(username) {

                    @Override
                    public void onValidationPassed() {
                        usernameTitle.setText("");
                    }

                    @Override
                    public void onValidationFailed(String reason) {
                        usernameTitle.setText(reason);
                    }
                },
                new Param<String>(password) {

                    @Override
                    public void onValidationPassed() {
                        passwordTitle.setText("");
                    }

                    @Override
                    public void onValidationFailed(String reason) {
                        passwordTitle.setText(reason);
                    }
                },
                new Param<RegistrationListener>(MainActivityRx.this)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        login.unsubscribe();
    }

    @Override
    public void onRegistrationSuccessful(RegistrationParams params) {
        Toast.makeText(MainActivityRx.this, "Registration successful!\n" + params.getUsername() + "\n" + params.getPassword(), Toast.LENGTH_LONG).show();
    }
}
