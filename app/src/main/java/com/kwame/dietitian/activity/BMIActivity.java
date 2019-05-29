package com.kwame.dietitian.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kwame.dietitian.R;

import java.text.DecimalFormat;

public class BMIActivity extends AppCompatActivity {

    private TextView bmiValue, bmiStatus;
    private EditText height, weight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        initView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.bmi));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bmiStatus = findViewById(R.id.bmi_status);
        bmiValue = findViewById(R.id.bmi_value);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);

        Button calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String userHeight = height.getText().toString();
        String userWeight = weight.getText().toString();

        if (TextUtils.isEmpty(userHeight) || TextUtils.isEmpty(userWeight))
            Toast.makeText(BMIActivity.this, "Weight and Height required", Toast.LENGTH_SHORT).show();
        else {
            double meters = Double.parseDouble(userHeight) / 100.0;
            double BMI = Double.parseDouble(userWeight) / (meters * meters);

            String status = "";
            if (BMI < 18.5)
                status = "Underweight";
            else if(BMI >=18.5 && BMI <=25)
                status = "Normal Weight";
            else if(BMI >25 && BMI <=30)
                status = "Over Weight";
            else if(BMI > 30 && BMI <=35)
                status = "Obesity (Level 1)";
            else if(BMI > 35 && BMI <=40)
                status = "Obesity (Level 2)";
            else if(BMI > 40)
                status = "Morbidly Obese";

            String stat = "BMI is " +new DecimalFormat("#.##").format(BMI);
            bmiValue.setText(stat);
            bmiStatus.setText(status);
            bmiStatus.setVisibility(View.VISIBLE);
            bmiValue.setVisibility(View.VISIBLE);
        }
    }
}
