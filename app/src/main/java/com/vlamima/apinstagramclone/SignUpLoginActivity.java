package com.vlamima.apinstagramclone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(view -> {
            ParseUser.logInInBackground(edtUsername.getText().toString(),
                    edtPassword.getText().toString(),
                    (user, e) -> {
                        if (user != null && e == null) {
                            FancyToast.makeText(SignUpLoginActivity.this,
                                    user.getUsername() + " logged in!", FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS, false).show();
                        } else {
                            FancyToast.makeText(SignUpLoginActivity.this,
                                    "Log in failed!\nCause: " + e.getMessage(), FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR, false).show();
                        }
            });
        });

        btnSignUp.setOnClickListener(view -> {
            ParseUser appUser = new ParseUser();
            appUser.setUsername(edtUsername.getText().toString());
            appUser.setPassword(edtPassword.getText().toString());
            appUser.signUpInBackground(e -> {
                if (e == null) {
                    FancyToast.makeText(SignUpLoginActivity.this,
                            "Account created!", FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS, false );
                } else {
                    FancyToast.makeText(SignUpLoginActivity.this,
                            "Sign up failed!\nerror: " + e.getMessage(), FancyToast.LENGTH_LONG,
                            FancyToast.ERROR, false );
                }
            });
        });
    }
}
