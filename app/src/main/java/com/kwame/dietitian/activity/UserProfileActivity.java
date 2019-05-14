package com.kwame.dietitian.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwame.dietitian.R;
import com.kwame.dietitian.util.UserPref;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private EditText email, age, name, height, weight;
    private UserPref userPref;
    private DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userPref = new UserPref(this);
        String id = userPref.getToken();
        reference = FirebaseDatabase.getInstance().getReference("users/"+id);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.edit_profile));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        name = findViewById(R.id.name);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);

        HashMap<String, String> user = userPref.getUserDetails();
        email.setText(user.get("email"));
        age.setText(user.get("age"));
        name.setText(user.get("name"));
        height.setText(user.get("height"));
        weight.setText(user.get("weight"));



        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserOtherDetails();
            }
        });


    }

    private void saveUserOtherDetails() {
        String userName = name.getText().toString();
        String userAge = age.getText().toString();
        String userHeight = height.getText().toString();
        String userWeight = weight.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userAge) || TextUtils.isEmpty(userHeight) || TextUtils.isEmpty(userWeight))
            Toast.makeText(UserProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        else {
            reference.child("name").setValue(userName);
            reference.child("age").setValue(userAge);
            reference.child("height").setValue(userHeight);
            reference.child("weight").setValue(userWeight);

            HashMap<String, String> data = new HashMap<>();
            data.put("name", userName);
            data.put("age", userAge);
            data.put("height", userHeight);
            data.put("weight", userWeight);

            userPref.storeOtherDetails(data);
            Toast.makeText(UserProfileActivity.this, "User Details Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
