package com.vlamima.apinstagramclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnSave;
    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;
    private TextView txtGetData;
    private Button getAll;
    private String allKickBoxers;
    private Button btnTransition;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);

        txtGetData = findViewById(R.id.txtGetData);
        txtGetData.setOnClickListener(v -> {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
            parseQuery.getInBackground("prgRAuSXDD", (object, e) -> {
                if (object != null && e == null) {
                    txtGetData.setText("Name: " + object.get("name") + "\n" +
                            "Punch power: " + object.get("punchPower") + "\n" +
                            "Kick power: " + object.get("kickPower"));
                }
            });
        });

        getAll = findViewById(R.id.btnGetAll);
        getAll.setOnClickListener(v -> {
            ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
            queryAll.whereGreaterThanOrEqualTo("punchPower", 10_000);
            queryAll.setLimit(1);
            queryAll.findInBackground((objects, e) -> {
                if (e == null) {
                    if (!objects.isEmpty()) {
                        String message = objects.stream().map(o -> o.get("name").toString()).reduce("",
                                (s1, s2) -> s1 + '\n' + s2);
                        FancyToast.makeText(SignUp.this, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }
                } else {
                    FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        });

        btnTransition = findViewById(R.id.btnNextActivity);
        btnTransition.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, SignUpLoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        try {
            ParseObject kickBoxer = new ParseObject("KickBoxer");
            kickBoxer.put("name", edtName.getText().toString());
            kickBoxer.put("punchSpeed", Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickBoxer.put("punchPower", Integer.parseInt(edtPunchPower.getText().toString()));
            kickBoxer.put("kickSpeed", Integer.parseInt(edtKickSpeed.getText().toString()));
            kickBoxer.put("kickPower", Integer.parseInt(edtKickPower.getText().toString()));
            kickBoxer.saveInBackground(e -> {
                if (e == null) {
                    //                Toast.makeText(SignUp.this, kickBoxer.get("name") + " is saved to the server", Toast.LENGTH_SHORT).show();
                    FancyToast.makeText(SignUp.this, kickBoxer.get("name") + " is saved to the server", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                } else {
                    FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }
}