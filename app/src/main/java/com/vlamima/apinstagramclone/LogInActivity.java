package com.vlamima.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        edtPassword.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                onClick(btnLogin);
            }
            return false;
        });

        btnLogin = findViewById(R.id.btnLogInLogin);
        btnLogin.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btnSignUpLogin);
        btnSignUp.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogInLogin:
                if (edtEmail.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("")) {

                    FancyToast.makeText(LogInActivity.this,
                            "All fields are required",
                            FancyToast.LENGTH_LONG,
                            FancyToast.INFO,
                            false).show();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging in");
                progressDialog.show();

                ParseUser.logInInBackground(edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        (user, e) -> {
                            if (user != null && e == null) {
                                FancyToast.makeText(this,
                                        user.getUsername() + " logged in",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false).show();
                                transitionToSocialMediaActivity();

                            } else {
                                FancyToast.makeText(this,
                                        "There was an error: " + e.getMessage(),
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,
                                        false).show();
                            }
                            progressDialog.dismiss();
                        });
                break;
            case R.id.btnSignUpLogin:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}