package com.vlamima.apinstagramclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign up");

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                onClick(btnSignUp);
            }
            return false;
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                if (edtEmail.getText().toString().equals("") ||
                    edtUsername.getText().toString().equals("") ||
                    edtPassword.getText().toString().equals("")) {

                    FancyToast.makeText(SignUp.this,
                            "All fields are required",
                            FancyToast.LENGTH_LONG,
                            FancyToast.INFO,
                            false).show();
                    return;
                }

                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtEmail.getText().toString());
                appUser.setUsername(edtUsername.getText().toString());
                appUser.setPassword(edtPassword.getText().toString());

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing up " + appUser.getUsername());
                progressDialog.show();

                appUser.signUpInBackground(e -> {
                   if (e == null) {
                       FancyToast.makeText(SignUp.this,
                               appUser.getUsername() + " is signed in",
                               FancyToast.LENGTH_SHORT,
                               FancyToast.SUCCESS, false).show();
                   } else {
                       FancyToast.makeText(SignUp.this,
                               "There was an error: " + e.getMessage(),
                               FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                   }

                   progressDialog.dismiss();
                });

                break;
            case R.id.btnLogIn:
                Intent intent = new Intent(SignUp.this, LogInActivity.class);
                startActivity(intent);
                break;
        }
    }
}