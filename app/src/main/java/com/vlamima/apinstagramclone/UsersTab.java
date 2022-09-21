package com.vlamima.apinstagramclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private List<String> usersUserName;
    private ArrayAdapter arrayAdapter;

    public UsersTab() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        usersUserName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, usersUserName);

        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        TextView txtUsersLoading = view.findViewById(R.id.txtUsersLoading);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("email", ParseUser.getCurrentUser().getEmail());
        userParseQuery.orderByAscending("username");

        userParseQuery.findInBackground((users, e) -> {
            if (e == null) {
                if (!users.isEmpty()) {
                    for (ParseUser user : users) {
                        usersUserName.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                    txtUsersLoading.animate().alpha(0).setDuration(2000);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UsersPosts.class);
        intent.putExtra("username", usersUserName.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", usersUserName.get(position));
        parseQuery.getFirstInBackground((user, e) -> {
            if (user != null && e == null) {
                new AlertDialog.Builder(getContext())
                        .setTitle(user.getUsername() + " info")
                        .setMessage(
                                "Bio: " + toStringNotNull(user.get("profileBio")) +
                                "\nProfession: " + toStringNotNull(user.get("profileProfession")) +
                                "\nHobbies: " + toStringNotNull(user.get("profileHobbies")) +
                                "\nFavourite sport: " + toStringNotNull(user.get("profileSport")))
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        return true;
    }

    private String toStringNotNull(Object object) {
        return (object != null ? object.toString() : "n/a");
    }
}