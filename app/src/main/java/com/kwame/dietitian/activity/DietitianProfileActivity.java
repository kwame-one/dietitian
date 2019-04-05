package com.kwame.dietitian.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kwame.dietitian.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DietitianProfileActivity extends AppCompatActivity {

    private Bundle bundle;
    private static final int CALL_PERMISSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietitian_profile);

        initUI();
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView phoneIcon = findViewById(R.id.phone);
        CircleImageView image = findViewById(R.id.image);
        ImageView webIcon = findViewById(R.id.web);
        TextView contact = findViewById(R.id.contact);
        TextView address = findViewById(R.id.address);
        TextView company = findViewById(R.id.company);
        TextView website = findViewById(R.id.website);
        TextView name = findViewById(R.id.name);
        TextView companyName = findViewById(R.id.company_name);

        bundle = getIntent().getExtras();

        if(bundle != null) {
            toolbar.setTitle(bundle.getString("name"));
            Picasso.get().load(bundle.getString("imageUrl")).placeholder(R.drawable.person).into(image);
            contact.setText(bundle.getString("contact"));
            address.setText(bundle.getString("address"));
            company.setText(bundle.getString("company"));
            companyName.setText(bundle.getString("company"));
            website.setText(bundle.getString("website"));
            name.setText(bundle.getString("name"));

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DietitianProfileActivity.this, ConsultDietitianActivity.class));
            }
        });

        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(DietitianProfileActivity.this, "Call Permission required", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void makeCall() {
        if (ContextCompat.checkSelfPermission(DietitianProfileActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DietitianProfileActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION);
        }else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+bundle.getString("contact")));
            startActivity(intent);
        }

    }


}
