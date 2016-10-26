package se.snylt.bouncer.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.snylt.bouncer.CheckResult;
import se.snylt.bouncer.Param;
import se.snylt.bouncer.android.api.Api;
import se.snylt.bouncer.android.api.RegistrationListener;
import se.snylt.bouncer.android.api.RegistrationParams;


public class MainActivityTraditional extends AppCompatActivity implements RegistrationListener {

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

    private final Api api = new Api();

    private CheckResult<Void> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        usernameEt.addTextChangedListener(createTextWatcher());
        usernameEt.setText("");
        passwordEt.addTextChangedListener(createTextWatcher());
        passwordEt.setText("");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.call();
            }
        });
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onInputChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void onInputChanged() {
        RegistrationParams params = createRegistrationParams(usernameEt.getText().toString(), passwordEt.getText().toString());
        result = api.register().check(params);
        registerBtn.setEnabled(result.isOk());
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
                new Param<RegistrationListener>(MainActivityTraditional.this)
        );
    }

    @Override
    public void onRegistrationSuccessful(RegistrationParams params) {
        Toast.makeText(MainActivityTraditional.this, "Registration successful!\n" + params.getUsername() + "\n" + params.getPassword(), Toast.LENGTH_LONG).show();
    }
}
