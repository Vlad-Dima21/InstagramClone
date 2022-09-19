package com.vlamima.apinstagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment {

    private EditText edtProfileName, edtProfileBio, edtProfileProfession, edtProfileHobbies,
        edtProfileSport;
    private Button btnUpdate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileSport = view.findViewById(R.id.edtProfileSport);

        ParseUser parseUser = ParseUser.getCurrentUser();

        edtProfileName.setText(parseUser.get("profileName") != null ? parseUser.get("profileName").toString() : "");
        edtProfileBio.setText(parseUser.get("profileBio") != null ? parseUser.get("profileBio").toString() : "");
        edtProfileProfession.setText(parseUser.get("profileProfession") != null ? parseUser.get("profileProfession").toString() : "");
        edtProfileHobbies.setText(parseUser.get("profileBio") != null ? parseUser.get("profileBio").toString() : "");
        edtProfileSport.setText(parseUser.get("profileSport") != null ? parseUser.get("profileSport").toString() : "");

        btnUpdate = view.findViewById(R.id.btnUpdateProfileInfo);
        btnUpdate.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Updating info");
            progressDialog.show();

            parseUser.put("profileName", edtProfileName.getText().toString());
            parseUser.put("profileBio", edtProfileBio.getText().toString());
            parseUser.put("profileProfession", edtProfileProfession.getText().toString());
            parseUser.put("profileHobbies", edtProfileHobbies.getText().toString());
            parseUser.put("profileSport", edtProfileSport.getText().toString());

            parseUser.saveInBackground(error -> {
                if (error == null) {
                    FancyToast.makeText(getContext(),
                            "Info saved",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.INFO,
                            false).show();
                } else {
                    FancyToast.makeText(getContext(),
                            error.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false).show();
                }
                progressDialog.dismiss();
            });
        });

        return view;
    }
}