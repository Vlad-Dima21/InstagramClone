package com.vlamima.apinstagramclone;

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
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener {

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
}