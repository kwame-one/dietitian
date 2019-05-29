package com.kwame.dietitian.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.kwame.dietitian.R;

import java.text.DecimalFormat;

public class BMRActivity extends AppCompatActivity {
    private EditText height, weight, age;
    private String gender= "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);

        initView();

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.bmr));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        age = findViewById(R.id.age);

        Button button = findViewById(R.id.calculate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMR();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean selected = ((RadioButton)view).isChecked();

        if (view.getId() == R.id.male)
           if (selected) {
               gender = "male";
           }
        else if (view.getId() == R.id.female)
            if (selected) {
                gender = "female";
            }
    }

    private void calculateBMR() {
        String userHeight = height.getText().toString();
        String userWeight = weight.getText().toString();
        String userAge = age.getText().toString();


        if (TextUtils.isEmpty(userHeight) || TextUtils.isEmpty(userWeight) || TextUtils.equals(gender, "") || TextUtils.isEmpty(userAge))
            Toast.makeText(BMRActivity.this, "Please all fields are required", Toast.LENGTH_SHORT).show();
        else{

            double BMR = 0;
            if (TextUtils.equals(gender, "male"))
                BMR = 66.47 + (13.7 * Double.parseDouble(userWeight)) + (5 * Double.parseDouble(userHeight) - (6.8 * Double.parseDouble(userAge))) ;
            else if (TextUtils.equals(gender, "female"))
                BMR = 655.1 + (9.6 * Double.parseDouble(userWeight)) + (1.8 * Double.parseDouble(userHeight) - (4.7 * Double.parseDouble(userAge))) ;

            TextView status = findViewById(R.id.bmr_value);
            String stat = "BMR is "+new DecimalFormat("#.##").format(BMR);
            status.setText(stat);
            status.setVisibility(View.VISIBLE);
        }
    }
}
