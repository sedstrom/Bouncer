package se.snylt.bouncer.android;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import android.graphics.Color;
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


public class MainActivityRx extends AppCompatActivity implements LoginListener {

    private final Api api = new Api();

    @BindView(R.id.usernameTitle)
    TextView usernameTitle;

    @BindView(R.id.usernameEt)
    EditText usernameEt;

    @BindView(R.id.passwordTitle)
    TextView passwordTitle;

    @BindView(R.id.passwordEt)
    EditText passwordEt;

    @BindView(R.id.loginBtn)
    Button loginBtn;

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
                // Create new params each time either of username or password input changes
                .combineLatest(
                        RxTextView.textChangeEvents(usernameEt),
                        RxTextView.textChangeEvents(passwordEt),
                        new Func2<TextViewTextChangeEvent, TextViewTextChangeEvent, CheckResult<Void>>() {
                            @Override
                            public CheckResult<Void> call(TextViewTextChangeEvent username, TextViewTextChangeEvent password) {
                                LoginParams params = createLoginParams(username.text().toString(), password.text().toString());
                                return api.login().check(params);
                            }
                        })
                // No need to let through invalid params
                .filter(new Func1<CheckResult<Void>, Boolean>() {
                    @Override
                    public Boolean call(CheckResult<Void> result) {
                        boolean valid = result.isOk();
                        loginBtn.setEnabled(valid);
                        return valid;
                    }
                })
                .onBackpressureLatest()
                // Button click + ok result = yey!
                .zipWith(RxView.clicks(loginBtn), new Func2<CheckResult<Void>, Void, CheckResult<Void>>() {
                    @Override
                    public CheckResult<Void> call(CheckResult<Void> result, Void aVoid) {
                        return result;
                    }
                })
                // A validated result + button click -> login
                .doOnNext(new Action1<CheckResult<Void>>() {
                    @Override
                    public void call(CheckResult<Void> result) {
                        result.call();
                    }
                }).subscribe();
    }

    private LoginParams createLoginParams(String username, String password) {
        return new LoginParams(
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
                new Param<String>(password) {

                    @Override
                    public void onValidationPassed() {
                        passwordTitle.setText("");
                        passwordTitle.setTextColor(Color.GREEN);
                    }

                    @Override
                    public void onValidationFailed(String reason) {
                        passwordTitle.setText(reason);
                        passwordTitle.setTextColor(Color.RED);
                    }
                },
                new Param<LoginListener>(MainActivityRx.this)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        login.unsubscribe();
    }

    @Override
    public void onLoginSuccessful() {
        Toast.makeText(MainActivityRx.this, "Login successful!", Toast.LENGTH_LONG).show();
    }
}
