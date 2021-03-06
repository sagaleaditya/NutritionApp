package ie.ul.csis.nutrition.user_interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import api.dto.accounts.AccountTokenDto;
import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.threading.networking.LoginRequest;
import ie.ul.csis.nutrition.utilities.Tools;



public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private ImageButton btnLogin;
    private Switch swtchRememberMe;
    private Context context;
    private ProgressDialog pDialog;

    private SharedPreferences preferenceData;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.log("LoginActivity", "onCreate", "OnCreate Called");

        setContentView(R.layout.activity_login);

        init();
        configureButtons();
    }

    private void init(){
        context = this;
        preferenceData = getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        editor = preferenceData.edit();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etRegisterPassword);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        swtchRememberMe = (Switch) findViewById(R.id.rememberMeSwitch);


        swtchRememberMe.setChecked(preferenceData.getBoolean(context.getString(R.string.rememberMeKey), false));

        if (preferenceData.getBoolean("rememberMe", false)) {
            etEmail.setText(preferenceData.getString("email", ""));
        }

        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Logging In");
        pDialog.setMessage("Please Wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
    }

    private void configureButtons(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Tools.toast(context, context.getString(R.string.error_invalid_email));
                    return;
                }

                if (swtchRememberMe.isChecked())
                {
                    editor.putString(context.getString(R.string.emailKey), email);
                    editor.putBoolean(context.getString(R.string.rememberMeKey), true);
                    editor.commit();
                }
                else
                {
                    editor.putString(context.getString(R.string.emailKey), email);
                    editor.putBoolean(context.getString(R.string.rememberMeKey), false);
                    editor.commit();
                }

                login(email, password);
            }
        });
    }

    public void login(final String email, final String password)
    {
        pDialog.show();
        AccountTokenDto dto = new AccountTokenDto(email, password);
        LoginRequest request = new LoginRequest(this);
        request.execute(dto);
    }



    public void changeToMainActivity()
    {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void changeToInternetDialog() {

        startActivity(new Intent(LoginActivity.this, InternetInformationActivity.class));
        finish();
    }


    public ProgressDialog getProgressDialog()
    {
        return pDialog;
    }

}
